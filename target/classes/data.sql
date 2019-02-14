INSERT INTO EMPRESA ( CNPJ, FANTASIA, CONTATO, ENABLED)
VALUES
      ( '03373373371264', 'Nacional Gas', '8532630001', 'S' ),
      ( '00073372371265', 'Petrolusa', '8532630002', 'S' ),
      ( '03373373371266', 'Texaco', '8532630004', 'S' ),
      ( '03373373371267', 'Tropigás', '8532640002', 'S' ),
      ( '03373373371268', 'Shell', '8532640001', 'S' );


INSERT INTO USER ( NAME, PASSWORD, MAIL, KEY, ENABLED, EMPRESA )
VALUES
      ('Administrador do Sistema', '$2a$10$Kuzptz154LDb96XHtJ6U/eLz4GAmaprVlnyi8rvTayo2AIrJwHdwC','admin@desafio.com.br', 'Idealit eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJhZG1pbkBkZXNhZmlvLmNvbS5iciIsImV4cCI6MTU1MDkwNDQ4Nn0.LiTZb5U-h6Qu0q5H9PMYtfNV44HKRgxU9T3u7zrCUuLqCVRGaabE7u4x-7l8OKhUrLHFW6MGsHIDZpXmQOJmZA', 'S', '03373373371264' ),
      ('José Carlos', '$2a$10$Kuzptz154LDb96XHtJ6U/eLz4GAmaprVlnyi8rvTayo2AIrJwHdwC','josecarlos@etc.br', 'Idealit eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJqb3NlY2FybG9zQGV0Yy5iciIsImV4cCI6MTU1MTA3MzUwOH0.gPYxivPWzNXS21KtcIAiRjxfBo4DzwMlxFVEVcaC_lyDogMcRBQ9vZcGtolZui6UP1eOJLmtjaP7N8_67dDIhw', 'S', '00073372371265' ),
      ('Patrícia Ramos', '$2a$10$Kuzptz154LDb96XHtJ6U/eLz4GAmaprVlnyi8rvTayo2AIrJwHdwC','pattyr@tal.br', 'Idealit eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJwYXR0eXJAdGFsLmJyIiwiZXhwIjoxNTUxMDczNTA4fQ.S_8N2U4QNHHpsKJ7fa_DEnCe2c5hp5YPgAP8yAWG86T6cpauKAiHMPOvQ63pDZgIHPkB6yucAS4y8yecq5mIoQ', 'S', '03373373371266' ),
      ('Zé Cantor', '$2a$10$Kuzptz154LDb96XHtJ6U/eLz4GAmaprVlnyi8rvTayo2AIrJwHdwC','zecantorr@texaco.co', 'Idealit eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJ6ZWNhbnRvcnJAdGV4YWNvLmNvIiwiZXhwIjoxNTUxMDczNTA4fQ.EnfDj9s7zQyFG13Kv46Ufwk0MWkYvrbbF-Dy9D4JKwOkS-iy1VgkQObEbx60qlvHqN-NIuwApaIl5osq3PjtxA', 'S', null ),
      ('SYS_SHELL', '$2a$10$Kuzptz154LDb96XHtJ6U/eLz4GAmaprVlnyi8rvTayo2AIrJwHdwC','system@shell.co', 'Idealit eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJzeXN0ZW1Ac2hlbGwuY28iLCJleHAiOjE1NTEwNzM1MDh9.mTSbjMDnqpPCiDjotE9t0DbhJbBR9O-6d5INXIZasQcqNGVZjZ-naQ60c9P3UoUydzr9dNWOtMgiXI4vHqKZ_w', 'S', null );

INSERT INTO ROLE (ID, NAME, DESCRIPTION)
VALUES
       ( 1, 'ADMIN', 'USUÁRIO ADMINISTRADOR' ),
       ( 2, 'SISTEMA', 'USUÁRIO SISTEMA' ),
       ( 3, 'POPULACAO', 'USUÁRIO POPULACAO');


INSERT INTO USER_ROLE (USER_ID, ROLE_ID)
VALUES
       ( 1, 1 ),
       ( 2, 3 ),
       ( 3, 3 ),
       ( 4, 1 ),
       ( 5, 2 );

INSERT INTO INCIDENTE ( NIVEL_PERIGO, COMENTARIO, DATA, STATUS, DATA_RESOLUCAO )
VALUES
       ( 'DANGER', 'Explosão de caldeira no setor 1', '2019-02-05 00:15:30', 'Em aberto', NULL ),
       ( 'WARNING', 'Caldeira 1 em período de revisão', '2019-02-05 00:15:30', 'Em aberto', NULL ),
       ( 'OK', 'Caldeira 3 revisada', '2019-02-05 00:15:30', 'Resolvido', '2019-02-05 00:17:15' ),
       ( 'OK', 'Caldeira 4 revisada', '2019-02-05 00:15:30', 'Resolvido', '2019-02-05 00:17:15' );


INSERT INTO EMPRESA_INCIDENTE ( CNPJ, INCIDENTE_ID)
VALUES
       ( '03373373371264', 1 ),
       ( '00073372371265', 2 ),
       ( '03373373371266', 3 ),
       ( '03373373371266', 4 );

