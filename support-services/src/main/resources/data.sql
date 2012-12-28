INSERT INTO settings
            (incremental_ticket_start_time)
SELECT Unix_timestamp(Sysdate()) - 300
FROM   DUAL
WHERE  NOT EXISTS (SELECT incremental_ticket_start_time
                   FROM   settings);