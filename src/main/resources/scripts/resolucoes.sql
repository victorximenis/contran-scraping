-- Table: public.resolucoes

-- DROP TABLE public.resolucoes;

CREATE TABLE public.resolucoes
(
    numero character varying(255) COLLATE pg_catalog."default",
    "dataResolucao" date,
    "dataPublicacao" date,
    assunto text COLLATE pg_catalog."default",
    situacao text COLLATE pg_catalog."default"
)

TABLESPACE pg_default;

ALTER TABLE public.resolucoes
    OWNER to postgres;