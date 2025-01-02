DROP TABLE IF EXISTS TB_CSTM;

CREATE TABLE TB_CSTM
(
  CSTM_ID     VARCHAR(20) not null,
  CSTM_NM   VARCHAR(100),
  CSTM_AGE   VARCHAR(3),
  CSTM_GND  VARCHAR(1),
  CSTM_PN    VARCHAR(20),
  CSTM_ADR  VARCHAR(1000)
)
;
comment on table TB_CSTM  is '고객';
comment on column TB_CSTM.CSTM_ID  is '고객번호';
comment on column TB_CSTM.CSTM_NM  is '고객명';
comment on column TB_CSTM.CSTM_AGE  is '나이';
comment on column TB_CSTM.CSTM_GND  is '성별';
comment on column TB_CSTM.CSTM_PN is '전화번호';
comment on column TB_CSTM.CSTM_ADR  is '주소';
alter table TB_CSTM
  add primary key (CSTM_ID);