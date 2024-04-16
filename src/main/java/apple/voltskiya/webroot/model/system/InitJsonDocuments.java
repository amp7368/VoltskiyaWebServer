package apple.voltskiya.webroot.model.system;

import apple.voltskiya.webroot.WebPlugin;
import apple.voltskiya.webroot.model.WebDatabase;
import apple.voltskiya.webroot.model.WebDatabaseModule;
import apple.voltskiya.webroot.model.json.DJsonDocument;
import apple.voltskiya.webroot.model.json.JsonFindApi;
import apple.voltskiya.webroot.model.json.JsonUpdateApi;
import apple.voltskiya.webroot.model.site.DSite;
import apple.voltskiya.webroot.model.site.SiteApi;
import io.ebean.Transaction;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.function.Predicate;
import java.util.stream.Stream;

public class InitJsonDocuments {

    private static final ExecutorService executor = Executors.newWorkStealingPool(50);

    public static void loadSitesAsync() {
        new Thread(InitJsonDocuments::loadSites).start();
    }

    private static void loadSites() {
        File root = WebPlugin.get().getRootFile("JSON");
        root.mkdirs();
        File[] files = root.listFiles();
        if (files == null) return;

        List<Future<List<Future<DJsonDocument>>>> processingDocuments = Arrays.stream(files)
            .filter(File::isDirectory)
            .map(siteFolder -> executor.submit(() -> loadSite(siteFolder)))
            .toList();
        Set<DJsonDocument> loadedDocuments = new HashSet<>();
        try {
            for (Future<List<Future<DJsonDocument>>> document : processingDocuments) {
                for (Future<DJsonDocument> doc : document.get()) {
                    loadedDocuments.add(doc.get());
                }
            }
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }

        List<? extends Future<?>> writingTasks = JsonFindApi.findAllDocuments().stream()
            .filter(Predicate.not(loadedDocuments::contains))
            .map(doc -> executor.submit(() -> InitJsonDocuments.writeDocument(doc)))
            .toList();
        for (Future<?> task : writingTasks) {
            try {
                task.get();
            } catch (InterruptedException | ExecutionException e) {
                throw new RuntimeException(e);
            }
        }
        WebDatabaseModule.get().logger().info("Finished loading all sites.");
    }

    private static void writeDocument(DJsonDocument document) {
        File file = document.getFile();
        file.getParentFile().mkdirs();
        try {
            Files.writeString(file.toPath(), document.getJsonString());
        } catch (IOException e) {
            WebDatabaseModule.get().logger().error("", e);
        }
    }

    private static List<Future<DJsonDocument>> loadSite(File siteFolder) {
        String msg = "Loading site '%s'".formatted(siteFolder.getName());
        WebDatabaseModule.get().logger().info(msg);
        DSite site = SiteApi.findOrCreate(siteFolder.getName());
        File docFolder = new File(siteFolder, "j");
        docFolder.mkdirs();
        return loadDocuments(site, "/", docFolder).toList();
    }

    private static Stream<Future<DJsonDocument>> loadDocuments(DSite site, String path, File root) {
        File[] files = root.listFiles();
        if (files == null) return Stream.empty();

        return Arrays.stream(files)
            .flatMap(file -> {
                String subPath = path + file.getName() + "/";
                if (file.isDirectory()) {
                    return loadDocuments(site, subPath, file);
                }
                String docPath = JsonFindApi.path(subPath);
                return Stream.of(executor.submit(
                    () -> loadSingleDocument(site, docPath, file)
                ));
            });
    }

    private static DJsonDocument loadSingleDocument(DSite site, String subPath, File file) {
        DJsonDocument document = JsonFindApi.findDocument(site, subPath);
        try (Transaction transaction = WebDatabase.db().beginTransaction()) {
            if (document == null) document = JsonUpdateApi.createEmpty(site, subPath, transaction);

            boolean doesFileMatch = JsonUpdateApi.updateDocument(document, file, transaction);
            transaction.commit();
            return doesFileMatch ? null : document;
        } catch (IOException e) {
            WebDatabaseModule.get().logger().error("", e);
        }
        return null;
    }
}
