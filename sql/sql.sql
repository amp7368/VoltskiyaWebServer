select *
from duser;
select *
from json_field
         left join
     json_document on json_field.document_id = json_document.id
where path like '%bff21586-147b-40b8-8a50-7362d3ac83c2%';


SELECT DISTINCT key_path,
                document_id,
                id,
                MAX(jf.version) OVER (PARTITION BY jf.key_path)                               AS version,
                FIRST_VALUE(jf.json)
                            OVER (PARTITION BY jf.key_path ORDER BY jf.version DESC)          AS json
FROM json_field jf
WHERE document_id = '465661e0-a810-4f67-86b4-f8ea8e006d52';

select *
from json_field jf
         left join main.json_document jd on jf.document_id = jd.id
where jd.path = 'Base/MobTypes/1017.json';

select *
from json_document;

select *
from json_field;

select *
from