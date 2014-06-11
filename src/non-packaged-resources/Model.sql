CREATE TABLE navigation.location (
       lid INT NOT NULL
     , label TEXT
     , PRIMARY KEY (lid)
);

CREATE TABLE marc.record (
       id INT NOT NULL
     , leader TEXT
     , c001 TEXT
     , c003 TEXT
     , c005 TEXT
     , c008 TEXT
     , c009 TEXT
     , PRIMARY KEY (id)
);

CREATE TABLE navigation.person (
       pid INT NOT NULL
     , first_name TEXT
     , last_name TEXT
     , PRIMARY KEY (pid)
);

CREATE TABLE marc.tag (
       id INT NOT NULL
     , tag TEXT NOT NULL
     , indicator1 TEXT
     , indicator2 TEXT
     , PRIMARY KEY (id, tag)
     , CONSTRAINT FK_tag_1 FOREIGN KEY (id)
                  REFERENCES marc.record (id)
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
     , CONSTRAINT FK_printer_2 FOREIGN KEY (pid)
                  REFERENCES navigation.person (pid)
     , CONSTRAINT FK_printer_3 FOREIGN KEY (id)
                  REFERENCES marc.record (id)
);

CREATE TABLE navigation.publisher (
       id INT NOT NULL
     , pid INT NOT NULL
     , PRIMARY KEY (id, pid)
     , CONSTRAINT FK_publisher_2 FOREIGN KEY (pid)
                  REFERENCES navigation.person (pid)
     , CONSTRAINT FK_publisher_3 FOREIGN KEY (id)
                  REFERENCES marc.record (id)
);

CREATE TABLE navigation.bookseller (
       id INT NOT NULL
     , pid INT NOT NULL
     , PRIMARY KEY (id, pid)
     , CONSTRAINT FK_bookseller_2 FOREIGN KEY (pid)
                  REFERENCES navigation.person (pid)
     , CONSTRAINT FK_bookseller_3 FOREIGN KEY (id)
                  REFERENCES marc.record (id)
);

CREATE TABLE estc.publication (
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
                  REFERENCES marc.record (id)
);

CREATE TABLE marc.subtag (
       id INT NOT NULL
     , tag TEXT NOT NULL
     , code TEXT NOT NULL
     , value TEXT
     , PRIMARY KEY (id, tag, code)
     , CONSTRAINT FK_subtag_1 FOREIGN KEY (id, tag)
                  REFERENCES marc.tag (id, tag)
);

CREATE TABLE navigation.author (
       id INT NOT NULL
     , pid INT NOT NULL
     , PRIMARY KEY (id, pid)
     , CONSTRAINT FK_author_2 FOREIGN KEY (pid)
                  REFERENCES navigation.person (pid)
     , CONSTRAINT FK_author_3 FOREIGN KEY (id)
                  REFERENCES marc.record (id)
);

