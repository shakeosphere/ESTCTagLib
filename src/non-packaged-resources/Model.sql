CREATE TABLE navigation.location (
       lid INT NOT NULL
     , label TEXT
     , PRIMARY KEY (lid)
);

CREATE TABLE navigation.record (
       id INT NOT NULL
     , leader TEXT
     , c001 TEXT
     , c003 TEXT
     , c005 TEXT
     , c008 TEXT
     , c009 TEXT
     , PRIMARY KEY (id)
);

CREATE TABLE moeml.gazetteer (
       moeml_id TEXT NOT NULL
     , title TEXT
     , PRIMARY KEY (moeml_id)
);

CREATE TABLE admin.user (
       id SERIAL NOT NULL
     , handle TEXT
     , password TEXT
     , is_approved BOOLEAN
     , is_admin BOOLEAN
     , first_name TEXT
     , last_name TEXT
     , email TEXT
     , created TIMESTAMP
     , last_login TIMESTAMP
     , PRIMARY KEY (id)
);

CREATE TABLE navigation.person (
       pid INT NOT NULL
     , first_name TEXT
     , last_name TEXT
     , PRIMARY KEY (pid)
);

CREATE TABLE navigation.mtag (
       id INT NOT NULL
     , tag TEXT NOT NULL
     , indicator1 TEXT
     , indicator2 TEXT
     , PRIMARY KEY (id, tag)
     , CONSTRAINT FK_tag_1 FOREIGN KEY (id)
                  REFERENCES navigation.record (id)
);

CREATE TABLE navigation.located (
       pid INT NOT NULL
     , lid INT NOT NULL
     , PRIMARY KEY (pid, lid)
     , CONSTRAINT FK_located_1 FOREIGN KEY (pid)
                  REFERENCES navigation.person (pid)
     , CONSTRAINT FK_located_2 FOREIGN KEY (lid)
                  REFERENCES navigation.location (lid)
);

CREATE TABLE navigation.printer (
       id INT NOT NULL
     , pid INT NOT NULL
     , PRIMARY KEY (id, pid)
     , CONSTRAINT FK_printer_1 FOREIGN KEY (id)
                  REFERENCES navigation.record (id)
     , CONSTRAINT FK_printer_2 FOREIGN KEY (pid)
                  REFERENCES navigation.person (pid)
);

CREATE TABLE navigation.publisher (
       id INT NOT NULL
     , pid INT NOT NULL
     , PRIMARY KEY (id, pid)
     , CONSTRAINT FK_publisher_1 FOREIGN KEY (id)
                  REFERENCES navigation.record (id)
     , CONSTRAINT FK_publisher_2 FOREIGN KEY (pid)
                  REFERENCES navigation.person (pid)
);

CREATE TABLE navigation.bookseller (
       id INT NOT NULL
     , pid INT NOT NULL
     , PRIMARY KEY (id, pid)
     , CONSTRAINT FK_bookseller_1 FOREIGN KEY (id)
                  REFERENCES navigation.record (id)
     , CONSTRAINT FK_bookseller_2 FOREIGN KEY (pid)
                  REFERENCES navigation.person (pid)
);

CREATE TABLE navigation.publication (
       id INT NOT NULL
     , rec_type TEXT
     , bib_level TEXT
     , multi_level TEXT
     , date_status TEXT
     , date1 TEXT
     , date2 TEXT
     , location TEXT
     , language TEXT
     , illustrations TEXT
     , form TEXT
     , title TEXT
     , remainder TEXT
     , extent TEXT
     , dimensions TEXT
     , other TEXT
     , gac TEXT
     , pub_location TEXT
     , publisher TEXT
     , PRIMARY KEY (id)
     , CONSTRAINT FK_publication_1 FOREIGN KEY (id)
                  REFERENCES navigation.record (id)
);

CREATE TABLE navigation.subtag (
       id INT NOT NULL
     , tag TEXT NOT NULL
     , code TEXT NOT NULL
     , value TEXT
     , PRIMARY KEY (id, tag, code)
     , CONSTRAINT FK_subtag_1 FOREIGN KEY (id, tag)
                  REFERENCES navigation.mtag (id, tag)
);

CREATE TABLE moeml.variant (
       moeml_id TEXT NOT NULL
     , seqnum INT NOT NULL
     , variant TEXT
     , PRIMARY KEY (moeml_id, seqnum)
     , CONSTRAINT FK_variant_1 FOREIGN KEY (moeml_id)
                  REFERENCES moeml.gazetteer (moeml_id)
);

CREATE TABLE moeml.match (
       moeml_id TEXT NOT NULL
     , seqnum INT NOT NULL
     , id INT NOT NULL
     , tag TEXT NOT NULL
     , PRIMARY KEY (moeml_id, seqnum, id, tag)
     , CONSTRAINT FK_match_2 FOREIGN KEY (id)
                  REFERENCES navigation.record (id)
     , CONSTRAINT FK_match_3 FOREIGN KEY (moeml_id)
                  REFERENCES moeml.gazetteer (moeml_id)
);

CREATE TABLE admin.session (
       id INTEGER NOT NULL
     , start TIMESTAMP NOT NULL
     , finish TIMESTAMP
     , PRIMARY KEY (id, start)
     , CONSTRAINT FK_session_1 FOREIGN KEY (id)
                  REFERENCES admin.user (id)
);

CREATE TABLE navigation.author (
       id INT NOT NULL
     , pid INT NOT NULL
     , PRIMARY KEY (id, pid)
     , CONSTRAINT FK_author_1 FOREIGN KEY (id)
                  REFERENCES navigation.record (id)
     , CONSTRAINT FK_author_2 FOREIGN KEY (pid)
                  REFERENCES navigation.person (pid)
);

