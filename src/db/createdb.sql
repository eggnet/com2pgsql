--
-- PostgreSQL database dump
--

SET statement_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SET check_function_bodies = false;
SET client_min_messages = warning;

CREATE EXTENSION IF NOT EXISTS plpgsql WITH SCHEMA pg_catalog;

--///////////////// People TABLE /////////////////--
CREATE SEQUENCE people_id_seq
	START WITH 1
	INCREMENT BY 1
	NO MINVALUE
	NO MAXVALUE
	CACHE 1;

CREATE TABLE people ( 
	p_id integer NOT NULL PRIMARY KEY DEFAULT NEXTVAL('people_id_seq'::regclass),
	name varchar(255) NOT NULL,
	email varchar(255) NOT NULL
);


ALTER SEQUENCE people_id_seq OWNED BY people.p_id;

--///////////////// ITEMS TABLE /////////////////-- 
CREATE SEQUENCE items_id_seq
	START WITH 1
	INCREMENT BY 1
	NO MINVALUE
	NO MAXVALUE
	CACHE 1;

CREATE TABLE items (
	p_id integer NOT NULL references people(p_id) ON DELETE CASCADE,
	item_date timestamp with time zone not null,
	item_id integer NOT NULL PRIMARY KEY DEFAULT NEXTVAL('items_id_seq'::regclass),
	body text,
	title text,
	type varchar(255) NOT NULL
);


ALTER SEQUENCE items_id_seq OWNED BY items.item_id;

--///////////////// THREADS TABLE /////////////////-- 

CREATE SEQUENCE threads_id_seq
	START WITH 1
	INCREMENT BY 1
	NO MINVALUE
	NO MAXVALUE
	CACHE 1;

CREATE TABLE threads (
	item_id integer NOT NULL references items(item_id) ON DELETE CASCADE,
	thread_id integer NOT NULL DEFAULT NEXTVAL('threads_id_seq'::regclass)
);

ALTER SEQUENCE threads_id_seq OWNED BY threads.thread_id;

--///////////////// REPLIES TABLE /////////////////-- 

CREATE TABLE replies (
	from_item_id integer NOT NULL references items(item_id) ON DELETE CASCADE,
	to_item_id integer NOT NULL references items(item_id) ON DELETE CASCADE
);

--///////////////// LINKS TABLE /////////////////-- 

CREATE TABLE links (
	item_id integer NOT NULL references items(item_id) ON DELETE CASCADE,
	commit_id varchar(255) NOT NULL,
	confidence real NOT NULL,
	PRIMARY KEY(item_id, commit_id)
);
