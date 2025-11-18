  CREATE TABLE clients (
      id VARCHAR(250) PRIMARY KEY,
      account INTEGER UNIQUE,
      agency INTEGER NOT NULL,
      name VARCHAR(200) NOT NULL,
      email VARCHAR(200) NOT NULL,
      password INTEGER NOT NULL
  );