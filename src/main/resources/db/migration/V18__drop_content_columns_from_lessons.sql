-- VXXX__drop_content_columns_from_lessons.sql

ALTER TABLE lessons
    DROP COLUMN IF EXISTS content_url,
    DROP COLUMN IF EXISTS content_type;
