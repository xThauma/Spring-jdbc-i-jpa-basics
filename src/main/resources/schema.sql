create table CUSTOMER (
  ID bigint primary key GENERATED BY DEFAULT AS IDENTITY,
  USERNAME varchar(50),
  PASSWORD varchar(50),
  NAME varchar(50),
  EMAIL varchar(50)
);