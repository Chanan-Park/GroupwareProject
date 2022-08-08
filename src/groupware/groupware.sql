
------- 기안문서 테이블 -------

create table tbl_report
(report_no        number -- 문서번호(기본키)
,fk_report_employee_id   VARCHAR2(3)        not null -- 사원번호
,rp_subject          Nvarchar2(100)  not null -- 제목
,rp_contents         Nvarchar2(200)  not null -- 내용
,writedate         date default sysdate not null --작성일자
,constraint PK_tbl_report_report_no primary key(report_no)
,constraint FK_tbl_report_fk_report_employee_id foreign key(fk_report_employee_id) references tbl_employees(employee_id)
);


select * from user_constraints where TABLE_NAME = 'TBL_REPORT';

select * from tbl_report;

--------------------------------------------------------------------------------

-- 기안문서 번호 시퀀스 --
create sequence seq_report_no
start with 1001
increment by 1
nomaxvalue
nominvalue
nocycle
nocache;

--------------------------------------------------------------------------------

--- 문서 결재 테이블 ---

create table tbl_approval
(approval_no              number    not null           -- 결재일련번호 
,fk_report_no             number    not null           -- 문서번호
,levelno                  number    not null           -- 결재단계번호
,fk_approval_employee_id  VARCHAR2(3) not null         -- 결재할사원번호 
,approval_status          number(1) default 0 not null -- 승인여부  0:미결  1:결재  2:반려  -1: 처리불가(아래에서 반려함)
,comments                 Nvarchar2(50)                -- 승인여부에 관한 코멘트
,approval_date            date  -- 결정한일자
,constraint PK_tbl_approval_approval_no  primary key(approval_no)
,constraint FK_tbl_approval_fk_report_no foreign key(fk_report_no) references tbl_report(report_no)
,constraint FK_tbl_approval_fk_approval_employee_id foreign key(fk_approval_employee_id) references tbl_employees(employee_id)
,constraint CK_tbl_approval_approval_status check( approval_status in (0,1,2,-1))
);

--------------------------------------------------------------------------------

--- 결재 번호 시퀀스 ---
create sequence seq_approval_no
start with 2001
increment by 1
nomaxvalue
nominvalue
nocycle
nocache;

--------------------------------------------------------------------------------

-- 결재할 사람 목록 받아오기 --
select employee_id as approval_employee_id
from tbl_employees
where level = 2
start with employee_id = '1' 
connect by prior manager_id = employee_id;


select * from tbl_report;
select * from tbl_approval;

delete from tbl_report;
delete from tbl_approval;
commit;

--------------------------------------------------------------------------------
-- 결재정보를 join하여 가져오는 뷰

create or replace view view_report_approval
as
with A as(
select report_no, fk_report_employee_id, department_id, name, rp_subject, rp_contents, writedate
from tbl_report R join tbl_employees E
on R.fk_report_employee_id = E.employee_id
),
B as (
select approval_no , fk_report_no, levelno, fk_approval_employee_id, department_id, name, approval_status, comments, approval_date
from tbl_approval A join tbl_employees E
on A.fk_approval_employee_id = E.employee_id
)
select report_no, approval_no, fk_report_employee_id as report_employee_id, A.name as report_name, A.department_id as report_department_id,
rp_subject, rp_contents, writedate, levelno, fk_approval_employee_id as approval_employee_id, B.name as approval_name, B.department_id as approval_department_id,
approval_status, comments, approval_date
from A join B
on A.report_no = B.fk_report_no;


--------------------------------------------------------------------------------
-- 결재 조회 프로시저
create or replace procedure pcd_reportInfo 
(p_employee_id IN tbl_employees.EMPLOYEE_ID%type, 
p_choice_number in varchar2, 
p_dept_id in tbl_employees.DEPARTMENT_ID%type, 
o_data out SYS_REFCURSOR)
is 
begin
    if (p_dept_id is not null) then
        if (p_choice_number = '1') then -- 진행중결재
            open o_data for
            select report_no, to_char(writedate, 'yyyy-mm-dd') as writedate, rp_subject, report_name,
            levelno, approval_name
            from view_report_approval
            where approval_status = 0 and REPORT_DEPARTMENT_ID = p_dept_id
            order by report_no desc, levelno;
                
        else -- 완료된결재
            open o_data for
                select report_no, to_char(writedate, 'yyyy-mm-dd') as writedate, rp_subject, report_name,
                levelno, approval_name, decode(approval_status, 1, '승인', 2, '반려', 3, '후결') as approval_status, to_char(approval_date, 'yyyy-mm-dd') as approval_date
                from view_report_approval
                where approval_status in (1, 2, 3) and REPORT_DEPARTMENT_ID = p_dept_id
                order by report_no desc, levelno; 
            
        end if;
        
    else
        if (p_choice_number = '1') then -- 진행중결재
            open o_data for
            select report_no, to_char(writedate, 'yyyy-mm-dd') as writedate, rp_subject, report_name,
            levelno, approval_name
            from view_report_approval
            where approval_status = 0
            order by report_no desc, levelno;
                
        else -- 완료된결재
            open o_data for
                select report_no, to_char(writedate, 'yyyy-mm-dd') as writedate, rp_subject, report_name,
                levelno, approval_name, decode(approval_status, 1, '승인', 2, '반려', 3, '후결') as approval_status, to_char(approval_date, 'yyyy-mm-dd') as approval_date
                from view_report_approval
                where approval_status in (1, 2, 3)
                order by report_no desc, levelno; 
            
        end if;
    end if;
end pcd_reportInfo;

select report_no, to_char(writedate, 'yyyy-mm-dd') as writedate, rp_subject, report_name,
            levelno, approval_name
            from view_report_approval
            where approval_status = 0 and REPORT_DEPARTMENT_ID = 500
            order by report_no desc, levelno;
--------------------------------------------------------------------------------

-- 결재 처리 프로시저
create or replace procedure pcd_tbl_approval_update
(p_report_no IN tbl_approval.fk_report_no%type
,p_approval_employee_id  in  tbl_approval.fk_approval_employee_id%type
,p_approval_status   in   tbl_approval.approval_status%type
,p_comments in tbl_approval.comments%type
)
is
begin
    -- 승인하기
    if(p_approval_status = 1) then
        update tbl_approval set approval_status = 1, comments = p_comments, approval_date = sysdate 
        where fk_report_no = p_report_no and fk_approval_employee_id = p_approval_employee_id;
    
    -- 반려하기
    elsif(p_approval_status = 2) then
        -- 내꺼는 반려
        update tbl_approval set approval_status = 2 where fk_report_no = p_report_no and fk_approval_employee_id = p_approval_employee_id;
        -- 상사꺼는 처리불가
        update tbl_approval set approval_status = -1 where fk_report_no = p_report_no and fk_approval_employee_id in
        (select employee_id from tbl_employees where level > 1 start with employee_id = p_approval_employee_id connect by prior manager_id = employee_id);
        update tbl_approval set comments = p_comments, approval_date = sysdate where fk_report_no = p_report_no and fk_approval_employee_id = p_approval_employee_id;

   end if;
end pcd_tbl_approval_update;

--------------------------------------------------------------------------------

-- 결재 가능 여부 조회 프로시저

create or replace procedure pcd_can_approve
(p_report_no IN tbl_approval.fk_report_no%type
,p_approval_employee_id  in  tbl_approval.fk_approval_employee_id%type
,o_approval out number
)
is
    v_myApproval number(1); -- 나의 결재여부
    v_myJunior_id varchar2(3); -- 부하직원
    v_juniorApproval number(1); -- 부하직원 결재여부
    v_myLevel number(1); -- 내 결재단계
    
begin
    -- 내가 결재 했는지 (안했으면 1 했으면 0)
    select count(*) into v_myApproval
    from tbl_approval
    where fk_report_no = p_report_no and fk_approval_employee_id = p_approval_employee_id and approval_status = 0;
    
    -- 내가 이미 결재 했으면
    if (v_myApproval = 0) then
        o_approval := 200;
        -- 결재 불가
        
    -- 내가 결재 안했으면
    -- 내 레벨알아오기
    else
        select levelno into v_myLevel from tbl_approval where fk_report_no = p_report_no and fk_approval_employee_id = p_approval_employee_id;
        
        -- 내가 첫번째 결재자라면
        if (v_myLevel = 1) then
             o_approval := 100;
            -- 결재 가능
           
        -- 내가 두번째 이상 결재자라면 
        else
            -- 직속 부하 사번 알아오기
            select fk_approval_employee_id into v_myJunior_id
            from tbl_approval
            where fk_report_no = p_report_no and 
            fk_approval_employee_id = ANY
            (
            select employee_id 
            from tbl_employees
            where level = 1
            start with manager_id = p_approval_employee_id
            connect by prior employee_id = manager_id);
            
            -- 내 부하직원이 결재 했는지 알아보기 (안했으면 1 했으면 0)
            select count(*) into v_juniorApproval
            from tbl_approval
            where fk_report_no = p_report_no and fk_approval_employee_id = v_myJunior_id and approval_status in (0,2);
                -- 부하직원이 결재안했거나 반려했으면 결재 불가
                if (v_juniorApproval = 1) then
                     o_approval := 300;
                -- 부하직원이 결재했으면 결재 가능
                else
                    o_approval := 100;
                end if;
        end if;
   end if;
end pcd_can_approve;
--------------------------------------------------------------------------------

-- 기안문서 내용 조회하기 프로시저

create or replace procedure pcd_view_report
(p_report_no in tbl_report.report_no%type
,p_department_id in tbl_employees.DEPARTMENT_ID%type
,o_data out SYS_REFCURSOR
)
is
    v_dept_id tbl_employees.DEPARTMENT_ID%type; -- 이 기안서가 어느 부서의 것인지
    error_dept     EXCEPTION; -- 다른 부서것을 조회하려고했을때 발생하는 에러
begin
    if (p_department_id is not null) then
    -- 조회하려는 사람이 사장님이 아니면
        
        -- 조회하려는 문서가 어느 부서 소유인지 검사
        select department_id into v_dept_id 
        from tbl_employees join tbl_report
        on employee_id = fk_report_employee_id
        where report_no = p_report_no;
        
        -- 다른부서의 문서를 조회하려하면 에러발생
        if v_dept_id != p_department_id then 
            raise error_dept;
        end if;
  end if;      
        -- 같은 부서 문서이면(혹은 사장님이면)
        open o_data for
        with A as (
        select department_name, fk_report_employee_id, report_no, rp_subject, rp_contents, name, writedate, POSITION
        from tbl_report join (select department_name, employee_id, name, position from tbl_departments d join tbl_employees e on d.department_id = e.department_id)
        on employee_id = fk_report_employee_id
        where report_no = p_report_no)
        , B as (
        select fk_report_no 
        from tbl_employees join(
        select fk_report_no, fk_approval_employee_id
        from tbl_approval
        where fk_report_no = p_report_no)
        on employee_id = fk_approval_employee_id)
        
        select distinct rp_subject ,rp_contents ,writedate, A.name as rp_name, fk_report_employee_id, department_name, POSITION
        from A join B on report_no = p_report_no;

EXCEPTION 
    WHEN error_dept THEN RAISE_APPLICATION_ERROR(-20001, '>> 다른 부서의 문서는 조회할 수 없습니다. <<');
    
end pcd_view_report;

--------------------------------------------------------------------------------

-- 기안문서 내용 수정하기 프로시저

create or replace procedure pcd_edit_report
(p_subject in tbl_report.rp_subject%type
,p_contents in tbl_report.rp_contents%type
,p_report_no in tbl_report.report_no%type
)
is
    v_approval_status tbl_approval.APPROVAL_STATUS%type; -- 이 문서의 결재 상태
    error_ongoing    exception; -- 이미 결재진행중인 문서를 수정하려고 했을 때 발생
begin
        -- 조회하려는 문서의 결재상태 알아오기
        select approval_status into v_approval_status
        from view_report_approval where report_no = p_report_no and levelno = 1;
                
        if v_approval_status = 0 then
            update tbl_report set rp_subject = p_subject, rp_contents = p_contents where report_no = p_report_no;
        else raise error_ongoing;
        
        end if;

EXCEPTION 
    WHEN error_ongoing THEN RAISE_APPLICATION_ERROR(-20003, '>> 이미 결재 진행중인 문서이므로 수정이 불가합니다. <<');
end pcd_edit_report;

--------------------------------------------------------------------------------

-- 기안문서 삭제하기 프로시저

create or replace procedure pcd_delete_report
(p_report_no in tbl_report.report_no%type)
is
    v_approval_status tbl_approval.APPROVAL_STATUS%type; -- 이 문서의 결재 상태
    error_ongoing    exception; -- 이미 결재진행중인 문서를 삭제하려고 했을 때 발생
begin
        -- 조회하려는 문서의 결재상태 알아오기
        select approval_status into v_approval_status
        from view_report_approval where report_no = p_report_no and levelno = 1;
        
        if v_approval_status = 0 then
            delete from tbl_report where report_no = p_report_no;
        else raise error_ongoing;
        
        end if;

EXCEPTION 
    WHEN error_ongoing THEN RAISE_APPLICATION_ERROR(-20003, '>> 이미 결재 진행중인 문서이므로 삭제가 불가합니다. <<');
end pcd_delete_report;

--------------------------------------------------------------------------------
