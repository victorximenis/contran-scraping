-- Table: public.portarias

-- DROP TABLE public.portarias;

CREATE TABLE public.portarias
(
    numero character varying(255) COLLATE pg_catalog."default",
    "dataPublicacao" date,
    assunto text COLLATE pg_catalog."default",
    observacao text COLLATE pg_catalog."default"
)

TABLESPACE pg_default;

ALTER TABLE public.portarias
    OWNER to postgres;