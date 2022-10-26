--
-- TOC entry 168 (class 1259 OID 11340421)
-- Name: academic_level; Type: TABLE; Schema: public; Owner: siette; Tablespace: 
--

CREATE TABLE academic_level (
    id bigint NOT NULL,
    code character varying(255) NOT NULL,
    description text,
    name character varying(255) NOT NULL
);


ALTER TABLE public.academic_level OWNER TO siette;

--
-- TOC entry 169 (class 1259 OID 11340431)
-- Name: academic_period; Type: TABLE; Schema: public; Owner: siette; Tablespace: 
--

CREATE TABLE academic_period (
    id bigint NOT NULL,
    code character varying(255) NOT NULL,
    description text,
    enddate timestamp without time zone NOT NULL,
    externalid character varying(255) NOT NULL,
    name character varying(255) NOT NULL,
    startdate timestamp without time zone NOT NULL
);


ALTER TABLE public.academic_period OWNER TO siette;

--
-- TOC entry 170 (class 1259 OID 11340443)
-- Name: address; Type: TABLE; Schema: public; Owner: siette; Tablespace: 
--

CREATE TABLE address (
    id bigint NOT NULL,
    city character varying(255) NOT NULL,
    name character varying(255) NOT NULL,
    number character varying(255) NOT NULL,
    phone character varying(255) NOT NULL,
    type character varying(255) NOT NULL,
    evaluationcenter_id bigint
);


ALTER TABLE public.address OWNER TO siette;

--
-- TOC entry 171 (class 1259 OID 11340451)
-- Name: available_state; Type: TABLE; Schema: public; Owner: siette; Tablespace: 
--

CREATE TABLE available_state (
    id bigint NOT NULL,
    value character varying(255) NOT NULL
);


ALTER TABLE public.available_state OWNER TO siette;

--
-- TOC entry 172 (class 1259 OID 11340456)
-- Name: bank; Type: TABLE; Schema: public; Owner: siette; Tablespace: 
--

CREATE TABLE bank (
    id bigint NOT NULL,
    createdate timestamp without time zone NOT NULL,
    currentnumber integer,
    externalid integer NOT NULL,
    iscomplete integer,
    name character varying(512) NOT NULL,
    questionnumber integer NOT NULL,
    state character varying(45) NOT NULL,
    department_id bigint
);


ALTER TABLE public.bank OWNER TO siette;

--
-- TOC entry 173 (class 1259 OID 11340466)
-- Name: cap; Type: TABLE; Schema: public; Owner: siette; Tablespace: 
--

CREATE TABLE cap (
    id bigint NOT NULL,
    key character varying(255) NOT NULL,
    name character varying(255) NOT NULL,
    serialnumber character varying(255) NOT NULL,
    ssid character varying(255) NOT NULL,
    evaluationcenter_id bigint NOT NULL,
    server_id bigint
);


ALTER TABLE public.cap OWNER TO siette;

--
-- TOC entry 174 (class 1259 OID 11340482)
-- Name: center; Type: TABLE; Schema: public; Owner: siette; Tablespace: 
--

CREATE TABLE center (
    id bigint NOT NULL,
    address character varying(255) NOT NULL,
    code character varying(255) NOT NULL,
    externalid character varying(255),
    name character varying(255) NOT NULL,
    stype character varying(255),
    type character varying(255),
    evaluationcenter_id bigint
);


ALTER TABLE public.center OWNER TO siette;

--
-- TOC entry 175 (class 1259 OID 11340492)
-- Name: classroom; Type: TABLE; Schema: public; Owner: siette; Tablespace: 
--

CREATE TABLE classroom (
    id bigint NOT NULL,
    createby character varying(255),
    createddate timestamp without time zone,
    lastmodifiedby character varying(255),
    lastmodifieddate timestamp without time zone,
    available boolean,
    location character varying(1024) NOT NULL,
    name character varying(256) NOT NULL,
    seats integer NOT NULL,
    cap_id bigint,
    evaluationcenter_id bigint,
    net_id bigint
);


ALTER TABLE public.classroom OWNER TO siette;

--
-- TOC entry 176 (class 1259 OID 11340500)
-- Name: classroom_time_block; Type: TABLE; Schema: public; Owner: siette; Tablespace: 
--

CREATE TABLE classroom_time_block (
    id bigint NOT NULL,
    createby character varying(255),
    createddate timestamp without time zone,
    lastmodifiedby character varying(255),
    lastmodifieddate timestamp without time zone,
    enddate timestamp without time zone NOT NULL,
    seats integer NOT NULL,
    startdate timestamp without time zone NOT NULL,
    available_state_id bigint NOT NULL,
    evaluation_event_classroom_id bigint NOT NULL,
    time_block_id bigint
);


ALTER TABLE public.classroom_time_block OWNER TO siette;

--
-- TOC entry 223 (class 1259 OID 11340855)
-- Name: classroom_time_block_student_type; Type: TABLE; Schema: public; Owner: siette; Tablespace: 
--

CREATE TABLE classroom_time_block_student_type (
    classroom_time_block_id bigint NOT NULL,
    student_type_id bigint NOT NULL
);


ALTER TABLE public.classroom_time_block_student_type OWNER TO siette;

--
-- TOC entry 177 (class 1259 OID 11340508)
-- Name: component; Type: TABLE; Schema: public; Owner: siette; Tablespace: 
--

CREATE TABLE component (
    id bigint NOT NULL,
    code character varying(24) NOT NULL,
    name character varying(256) NOT NULL,
    academiclevel_id bigint,
    academicperiod_id bigint NOT NULL,
    manager_id bigint,
    mode_id bigint
);


ALTER TABLE public.component OWNER TO siette;

--
-- TOC entry 178 (class 1259 OID 11340515)
-- Name: department; Type: TABLE; Schema: public; Owner: siette; Tablespace: 
--

CREATE TABLE department (
    id bigint NOT NULL,
    name character varying(512) NOT NULL
);


ALTER TABLE public.department OWNER TO siette;

--
-- TOC entry 179 (class 1259 OID 11340523)
-- Name: doc_file; Type: TABLE; Schema: public; Owner: siette; Tablespace: 
--

CREATE TABLE doc_file (
    id bigint NOT NULL,
    createby character varying(255),
    createddate timestamp without time zone,
    lastmodifiedby character varying(255),
    lastmodifieddate timestamp without time zone,
    classroomcode character varying(255),
    emailtonotify character varying(255),
    enddatetest timestamp without time zone,
    evaluationcentercode character varying(255),
    evaluationeventcode character varying(255) NOT NULL,
    filename character varying(255) NOT NULL,
    filepath character varying(255),
    startdatetest timestamp without time zone
);


ALTER TABLE public.doc_file OWNER TO siette;

--
-- TOC entry 180 (class 1259 OID 11340531)
-- Name: email_notifications; Type: TABLE; Schema: public; Owner: siette; Tablespace: 
--

CREATE TABLE email_notifications (
    id bigint NOT NULL,
    read timestamp without time zone,
    sent timestamp without time zone NOT NULL,
    token character varying(255) NOT NULL,
    evaluationevent_id bigint,
    user_id bigint
);


ALTER TABLE public.email_notifications OWNER TO siette;

--
-- TOC entry 181 (class 1259 OID 11340536)
-- Name: evaluation_assignment; Type: TABLE; Schema: public; Owner: siette; Tablespace: 
--

CREATE TABLE evaluation_assignment (
    id bigint NOT NULL,
    externalpassword character varying(255),
    evaluationevent_id bigint,
    studenttype_id bigint,
    user_id bigint
);


ALTER TABLE public.evaluation_assignment OWNER TO siette;

--
-- TOC entry 182 (class 1259 OID 11340543)
-- Name: evaluation_assignment_matter; Type: TABLE; Schema: public; Owner: siette; Tablespace: 
--

CREATE TABLE evaluation_assignment_matter (
    id bigint NOT NULL,
    career character varying(255),
    careercode character varying(255),
    channel character varying(255),
    center_id bigint,
    evaluationassignment_id bigint,
    evaluationeventmatter_id bigint
);


ALTER TABLE public.evaluation_assignment_matter OWNER TO siette;

--
-- TOC entry 183 (class 1259 OID 11340551)
-- Name: evaluation_center; Type: TABLE; Schema: public; Owner: siette; Tablespace: 
--

CREATE TABLE evaluation_center (
    id bigint NOT NULL,
    createby character varying(255),
    createddate timestamp without time zone,
    lastmodifiedby character varying(255),
    lastmodifieddate timestamp without time zone,
    code character varying(256) NOT NULL,
    description character varying(1024),
    name character varying(256) NOT NULL,
    transport text
);


ALTER TABLE public.evaluation_center OWNER TO siette;

--
-- TOC entry 184 (class 1259 OID 11340563)
-- Name: evaluation_event; Type: TABLE; Schema: public; Owner: siette; Tablespace: 
--

CREATE TABLE evaluation_event (
    id bigint NOT NULL,
    code character varying(100) NOT NULL,
    comment text,
    enddate timestamp without time zone NOT NULL,
    name character varying(250) NOT NULL,
    startdate timestamp without time zone NOT NULL,
    state character varying(64),
    testduration integer NOT NULL
);


ALTER TABLE public.evaluation_event OWNER TO siette;

--
-- TOC entry 224 (class 1259 OID 11340860)
-- Name: evaluation_event_admin; Type: TABLE; Schema: public; Owner: siette; Tablespace: 
--

CREATE TABLE evaluation_event_admin (
    evaluation_event_id bigint NOT NULL,
    user_id bigint NOT NULL
);


ALTER TABLE public.evaluation_event_admin OWNER TO siette;

--
-- TOC entry 185 (class 1259 OID 11340573)
-- Name: evaluation_event_classroom; Type: TABLE; Schema: public; Owner: siette; Tablespace: 
--

CREATE TABLE evaluation_event_classroom (
    id bigint NOT NULL,
    seats integer NOT NULL,
    cap_id bigint NOT NULL,
    classroom_id bigint NOT NULL,
    evaluation_event_evaluation_center_id bigint NOT NULL,
    net_id bigint,
    schedule_id bigint NOT NULL
);


ALTER TABLE public.evaluation_event_classroom OWNER TO siette;

--
-- TOC entry 186 (class 1259 OID 11340578)
-- Name: evaluation_event_configuration; Type: TABLE; Schema: public; Owner: siette; Tablespace: 
--

CREATE TABLE evaluation_event_configuration (
    id bigint NOT NULL,
    assignmenttype character varying(255),
    dailyevaluationschedulesmailingenddate timestamp without time zone,
    dailyevaluationschedulesmailingstartdate timestamp without time zone,
    dailyevaluationschedulesmailingtime timestamp without time zone,
    dailystudentsandschedulesloadenddate timestamp without time zone,
    dailystudentsandschedulesloadstartdate timestamp without time zone,
    dailystudentsandschedulesloadtime timestamp without time zone,
    maxdailytests integer NOT NULL,
    maxtimeblocktests integer NOT NULL,
    testsinseveraltimeblockssameday boolean NOT NULL,
    evaluation_event_id bigint NOT NULL
);


ALTER TABLE public.evaluation_event_configuration OWNER TO siette;

--
-- TOC entry 187 (class 1259 OID 11340583)
-- Name: evaluation_event_evaluation_center; Type: TABLE; Schema: public; Owner: siette; Tablespace: 
--

CREATE TABLE evaluation_event_evaluation_center (
    id bigint NOT NULL,
    evaluation_center_id bigint,
    evaluation_event_id bigint
);


ALTER TABLE public.evaluation_event_evaluation_center OWNER TO siette;

--
-- TOC entry 188 (class 1259 OID 11340588)
-- Name: evaluation_event_evaluation_center_center; Type: TABLE; Schema: public; Owner: siette; Tablespace: 
--

CREATE TABLE evaluation_event_evaluation_center_center (
    id bigint NOT NULL,
    center_id bigint,
    evaluation_event_evaluation_center_id bigint
);


ALTER TABLE public.evaluation_event_evaluation_center_center OWNER TO siette;

--
-- TOC entry 225 (class 1259 OID 11340865)
-- Name: evaluation_event_evaluation_type; Type: TABLE; Schema: public; Owner: siette; Tablespace: 
--

CREATE TABLE evaluation_event_evaluation_type (
    evaluation_event_id bigint NOT NULL,
    evaluation_type_id bigint NOT NULL
);


ALTER TABLE public.evaluation_event_evaluation_type OWNER TO siette;

--
-- TOC entry 189 (class 1259 OID 11340595)
-- Name: evaluation_event_matter; Type: TABLE; Schema: public; Owner: siette; Tablespace: 
--

CREATE TABLE evaluation_event_matter (
    id bigint NOT NULL,
    extrascore real,
    academicperiod_id bigint,
    bank_id bigint,
    evaluationevent_id bigint,
    matter_id bigint,
    mode_id bigint
);


ALTER TABLE public.evaluation_event_matter OWNER TO siette;

--
-- TOC entry 226 (class 1259 OID 11340870)
-- Name: evaluation_event_matter_day; Type: TABLE; Schema: public; Owner: siette; Tablespace: 
--

CREATE TABLE evaluation_event_matter_day (
    evaluationeventmatter_id bigint NOT NULL,
    daysallowed_id bigint NOT NULL
);


ALTER TABLE public.evaluation_event_matter_day OWNER TO siette;

--
-- TOC entry 227 (class 1259 OID 11340875)
-- Name: evaluation_event_matter_evaluation_type; Type: TABLE; Schema: public; Owner: siette; Tablespace: 
--

CREATE TABLE evaluation_event_matter_evaluation_type (
    evaluationeventmatter_id bigint NOT NULL,
    evaluation_type_id bigint NOT NULL
);


ALTER TABLE public.evaluation_event_matter_evaluation_type OWNER TO siette;

--
-- TOC entry 190 (class 1259 OID 11340602)
-- Name: evaluation_event_matter_test; Type: TABLE; Schema: public; Owner: siette; Tablespace: 
--

CREATE TABLE evaluation_event_matter_test (
    id bigint NOT NULL,
    enablepublish integer,
    extrascore real,
    extrascoreapplied integer,
    state character varying(255),
    evaluationeventmatter_id bigint,
    test_id bigint
);


ALTER TABLE public.evaluation_event_matter_test OWNER TO siette;

--
-- TOC entry 191 (class 1259 OID 11340609)
-- Name: evaluation_event_teacher; Type: TABLE; Schema: public; Owner: siette; Tablespace: 
--

CREATE TABLE evaluation_event_teacher (
    id bigint NOT NULL,
    evaluation_event_id bigint,
    user_id bigint
);


ALTER TABLE public.evaluation_event_teacher OWNER TO siette;

--
-- TOC entry 228 (class 1259 OID 11340880)
-- Name: evaluation_event_teacher_classroom_time_block; Type: TABLE; Schema: public; Owner: siette; Tablespace: 
--

CREATE TABLE evaluation_event_teacher_classroom_time_block (
    classroom_time_block_id bigint NOT NULL,
    evaluation_event_teacher_id bigint NOT NULL
);


ALTER TABLE public.evaluation_event_teacher_classroom_time_block OWNER TO siette;

--
-- TOC entry 192 (class 1259 OID 11340614)
-- Name: evaluation_type; Type: TABLE; Schema: public; Owner: siette; Tablespace: 
--

CREATE TABLE evaluation_type (
    id bigint NOT NULL,
    code character varying(255) NOT NULL,
    description text,
    name character varying(255) NOT NULL
);


ALTER TABLE public.evaluation_type OWNER TO siette;

--
-- TOC entry 193 (class 1259 OID 11340624)
-- Name: evs_user; Type: TABLE; Schema: public; Owner: siette; Tablespace: 
--

CREATE TABLE evs_user (
    id bigint NOT NULL,
    email character varying(255),
    enabled integer NOT NULL,
    externalid character varying(255),
    firstname character varying(255) NOT NULL,
    identification character varying(255),
    lastname character varying(255) NOT NULL,
    password character varying(255),
    username character varying(255) NOT NULL
);


ALTER TABLE public.evs_user OWNER TO siette;

--
-- TOC entry 194 (class 1259 OID 11340634)
-- Name: generic_schedule_log; Type: TABLE; Schema: public; Owner: siette; Tablespace: 
--

CREATE TABLE generic_schedule_log (
    id bigint NOT NULL,
    createby character varying(255),
    createddate timestamp without time zone,
    lastmodifiedby character varying(255),
    lastmodifieddate timestamp without time zone,
    extra text,
    message text NOT NULL,
    read boolean NOT NULL,
    type character varying(255) NOT NULL,
    evaluation_event_id bigint NOT NULL
);


ALTER TABLE public.generic_schedule_log OWNER TO siette;

--
-- TOC entry 235 (class 1259 OID 11341430)
-- Name: hibernate_sequence; Type: SEQUENCE; Schema: public; Owner: siette
--

CREATE SEQUENCE hibernate_sequence
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.hibernate_sequence OWNER TO siette;

--
-- TOC entry 195 (class 1259 OID 11340642)
-- Name: matter; Type: TABLE; Schema: public; Owner: siette; Tablespace: 
--

CREATE TABLE matter (
    id bigint NOT NULL,
    code character varying(256) NOT NULL,
    name character varying(256) NOT NULL,
    type character varying(256),
    academiclevel_id bigint,
    user_id bigint
);


ALTER TABLE public.matter OWNER TO siette;

--
-- TOC entry 196 (class 1259 OID 11340652)
-- Name: matter_bank; Type: TABLE; Schema: public; Owner: siette; Tablespace: 
--

CREATE TABLE matter_bank (
    matter_id bigint NOT NULL,
    bank_id bigint NOT NULL
);


ALTER TABLE public.matter_bank OWNER TO siette;

--
-- TOC entry 229 (class 1259 OID 11340885)
-- Name: matter_day; Type: TABLE; Schema: public; Owner: siette; Tablespace: 
--

CREATE TABLE matter_day (
    matter_id bigint NOT NULL,
    daysallowed_id bigint NOT NULL
);


ALTER TABLE public.matter_day OWNER TO siette;

--
-- TOC entry 197 (class 1259 OID 11340657)
-- Name: matter_subject; Type: TABLE; Schema: public; Owner: siette; Tablespace: 
--

CREATE TABLE matter_subject (
    matter_id bigint NOT NULL,
    subject_id bigint NOT NULL
);


ALTER TABLE public.matter_subject OWNER TO siette;

--
-- TOC entry 198 (class 1259 OID 11340662)
-- Name: matter_test_student; Type: TABLE; Schema: public; Owner: siette; Tablespace: 
--

CREATE TABLE matter_test_student (
    id bigint NOT NULL,
    externalpassword character varying(255),
    evaluationassignmentmatter_id bigint,
    evaluationeventmattertest_id bigint,
    session_id bigint,
    studentevaluation_id bigint
);


ALTER TABLE public.matter_test_student OWNER TO siette;

--
-- TOC entry 199 (class 1259 OID 11340669)
-- Name: mode; Type: TABLE; Schema: public; Owner: siette; Tablespace: 
--

CREATE TABLE mode (
    id bigint NOT NULL,
    code character varying(255) NOT NULL,
    createdate timestamp without time zone NOT NULL,
    description text,
    name character varying(255) NOT NULL
);


ALTER TABLE public.mode OWNER TO siette;

--
-- TOC entry 200 (class 1259 OID 11340679)
-- Name: net; Type: TABLE; Schema: public; Owner: siette; Tablespace: 
--

CREATE TABLE net (
    id bigint NOT NULL,
    code character varying(124) NOT NULL,
    name character varying(512) NOT NULL,
    password character varying(124) NOT NULL,
    evaluationcenter_id bigint
);


ALTER TABLE public.net OWNER TO siette;

--
-- TOC entry 230 (class 1259 OID 11340890)
-- Name: net_server; Type: TABLE; Schema: public; Owner: siette; Tablespace: 
--

CREATE TABLE net_server (
    net_id_id bigint NOT NULL,
    server_id bigint NOT NULL
);


ALTER TABLE public.net_server OWNER TO siette;

--
-- TOC entry 201 (class 1259 OID 11340689)
-- Name: permission; Type: TABLE; Schema: public; Owner: siette; Tablespace: 
--

CREATE TABLE permission (
    id bigint NOT NULL,
    domainobject character varying(255),
    objectid bigint,
    permission character varying(255) NOT NULL
);


ALTER TABLE public.permission OWNER TO siette;

--
-- TOC entry 202 (class 1259 OID 11340697)
-- Name: registration; Type: TABLE; Schema: public; Owner: siette; Tablespace: 
--

CREATE TABLE registration (
    id bigint NOT NULL,
    center_id bigint,
    user_id bigint
);


ALTER TABLE public.registration OWNER TO siette;

--
-- TOC entry 231 (class 1259 OID 11340895)
-- Name: registration_subject; Type: TABLE; Schema: public; Owner: siette; Tablespace: 
--

CREATE TABLE registration_subject (
    registration_id bigint NOT NULL,
    subject_id bigint NOT NULL
);


ALTER TABLE public.registration_subject OWNER TO siette;

--
-- TOC entry 203 (class 1259 OID 11340704)
-- Name: role; Type: TABLE; Schema: public; Owner: siette; Tablespace: 
--

CREATE TABLE role (
    id bigint NOT NULL,
    code character varying(255) NOT NULL,
    name character varying(255) NOT NULL
);


ALTER TABLE public.role OWNER TO siette;

--
-- TOC entry 204 (class 1259 OID 11340712)
-- Name: schedule; Type: TABLE; Schema: public; Owner: siette; Tablespace: 
--

CREATE TABLE schedule (
    id bigint NOT NULL,
    name character varying(250) NOT NULL,
    evaluation_event_id bigint NOT NULL
);


ALTER TABLE public.schedule OWNER TO siette;

--
-- TOC entry 205 (class 1259 OID 11340717)
-- Name: schedule_modification; Type: TABLE; Schema: public; Owner: siette; Tablespace: 
--

CREATE TABLE schedule_modification (
    id bigint NOT NULL,
    createby character varying(255),
    createddate timestamp without time zone,
    lastmodifiedby character varying(255),
    lastmodifieddate timestamp without time zone,
    message text NOT NULL,
    evaluation_assignment_id bigint NOT NULL
);


ALTER TABLE public.schedule_modification OWNER TO siette;

--
-- TOC entry 206 (class 1259 OID 11340725)
-- Name: schedule_modification_date; Type: TABLE; Schema: public; Owner: siette; Tablespace: 
--

CREATE TABLE schedule_modification_date (
    id bigint NOT NULL,
    enddate timestamp without time zone NOT NULL,
    startdate timestamp without time zone NOT NULL,
    evaluation_event_id bigint NOT NULL
);


ALTER TABLE public.schedule_modification_date OWNER TO siette;

--
-- TOC entry 207 (class 1259 OID 11340730)
-- Name: schedule_modification_info; Type: TABLE; Schema: public; Owner: siette; Tablespace: 
--

CREATE TABLE schedule_modification_info (
    id bigint NOT NULL,
    matter_test_student_id bigint NOT NULL,
    new_classroom_time_block_id bigint NOT NULL,
    old_classroom_time_block_id bigint NOT NULL,
    schedule_modification_id bigint NOT NULL
);


ALTER TABLE public.schedule_modification_info OWNER TO siette;

--
-- TOC entry 208 (class 1259 OID 11340735)
-- Name: server; Type: TABLE; Schema: public; Owner: siette; Tablespace: 
--

CREATE TABLE server (
    id bigint NOT NULL,
    code character varying(124) NOT NULL,
    name character varying(512) NOT NULL,
    url character varying(512) NOT NULL
);


ALTER TABLE public.server OWNER TO siette;

--
-- TOC entry 209 (class 1259 OID 11340745)
-- Name: session; Type: TABLE; Schema: public; Owner: siette; Tablespace: 
--

CREATE TABLE session (
    id bigint NOT NULL,
    externalid bigint,
    nblanks integer NOT NULL,
    nfails integer NOT NULL,
    nsuccesses integer NOT NULL,
    rate real NOT NULL,
    state character varying(255),
    testdate timestamp without time zone,
    urltoken character varying(255)
);


ALTER TABLE public.session OWNER TO siette;

--
-- TOC entry 210 (class 1259 OID 11340753)
-- Name: student_evaluation; Type: TABLE; Schema: public; Owner: siette; Tablespace: 
--

CREATE TABLE student_evaluation (
    id bigint NOT NULL,
    createby character varying(255),
    createddate timestamp without time zone,
    lastmodifiedby character varying(255),
    lastmodifieddate timestamp without time zone,
    classroom_time_block_id bigint,
    evaluation_assignment_id bigint,
    net_id bigint,
    server_id bigint
);


ALTER TABLE public.student_evaluation OWNER TO siette;

--
-- TOC entry 211 (class 1259 OID 11340761)
-- Name: student_test_schedule_log; Type: TABLE; Schema: public; Owner: siette; Tablespace: 
--

CREATE TABLE student_test_schedule_log (
    id bigint NOT NULL,
    createby character varying(255),
    createddate timestamp without time zone,
    lastmodifiedby character varying(255),
    lastmodifieddate timestamp without time zone,
    extra text,
    message text NOT NULL,
    read boolean NOT NULL,
    type character varying(255) NOT NULL,
    classroom_time_block_id bigint,
    evaluation_assignment_id bigint NOT NULL,
    matter_test_student_id bigint NOT NULL
);


ALTER TABLE public.student_test_schedule_log OWNER TO siette;

--
-- TOC entry 212 (class 1259 OID 11340769)
-- Name: student_type; Type: TABLE; Schema: public; Owner: siette; Tablespace: 
--

CREATE TABLE student_type (
    id bigint NOT NULL,
    value character varying(255) NOT NULL
);


ALTER TABLE public.student_type OWNER TO siette;

--
-- TOC entry 213 (class 1259 OID 11340776)
-- Name: subject; Type: TABLE; Schema: public; Owner: siette; Tablespace: 
--

CREATE TABLE subject (
    id bigint NOT NULL,
    description text,
    name character varying(255) NOT NULL,
    type character varying(255) NOT NULL
);


ALTER TABLE public.subject OWNER TO siette;

--
-- TOC entry 214 (class 1259 OID 11340784)
-- Name: test; Type: TABLE; Schema: public; Owner: siette; Tablespace: 
--

CREATE TABLE test (
    id bigint NOT NULL,
    active integer,
    createdate timestamp without time zone,
    externalid integer,
    locker character varying(255),
    maxquestionnum integer,
    minquestionnum integer,
    name character varying(255) NOT NULL,
    "time" integer,
    bank_id bigint,
    evaluationtype_id bigint,
    testtype_id bigint
);


ALTER TABLE public.test OWNER TO siette;

--
-- TOC entry 215 (class 1259 OID 11340794)
-- Name: test_sync; Type: TABLE; Schema: public; Owner: siette; Tablespace: 
--

CREATE TABLE test_sync (
    id bigint NOT NULL,
    createby character varying(255),
    createddate timestamp without time zone,
    lastmodifiedby character varying(255),
    lastmodifieddate timestamp without time zone,
    description character varying(255),
    state character varying(255),
    evaluationevent_id bigint
);


ALTER TABLE public.test_sync OWNER TO siette;

--
-- TOC entry 216 (class 1259 OID 11340802)
-- Name: test_type; Type: TABLE; Schema: public; Owner: siette; Tablespace: 
--

CREATE TABLE test_type (
    id bigint NOT NULL,
    formula character varying(255) NOT NULL,
    maxnumquestion integer NOT NULL,
    maxrate real NOT NULL,
    name character varying(255) NOT NULL
);


ALTER TABLE public.test_type OWNER TO siette;

--
-- TOC entry 217 (class 1259 OID 11340810)
-- Name: time_block; Type: TABLE; Schema: public; Owner: siette; Tablespace: 
--

CREATE TABLE time_block (
    id bigint NOT NULL,
    enddate timestamp without time zone NOT NULL,
    startdate timestamp without time zone NOT NULL,
    schedule_id bigint NOT NULL
);


ALTER TABLE public.time_block OWNER TO siette;

--
-- TOC entry 232 (class 1259 OID 11340900)
-- Name: time_block_student_type; Type: TABLE; Schema: public; Owner: siette; Tablespace: 
--

CREATE TABLE time_block_student_type (
    time_block_id bigint NOT NULL,
    student_type_id bigint NOT NULL
);


ALTER TABLE public.time_block_student_type OWNER TO siette;

--
-- TOC entry 218 (class 1259 OID 11340815)
-- Name: transport; Type: TABLE; Schema: public; Owner: siette; Tablespace: 
--

CREATE TABLE transport (
    id bigint NOT NULL,
    code character varying(255) NOT NULL,
    name character varying(255) NOT NULL
);


ALTER TABLE public.transport OWNER TO siette;

--
-- TOC entry 233 (class 1259 OID 11340905)
-- Name: user_permission; Type: TABLE; Schema: public; Owner: siette; Tablespace: 
--

CREATE TABLE user_permission (
    user_id bigint NOT NULL,
    permission_id bigint NOT NULL
);


ALTER TABLE public.user_permission OWNER TO siette;

--
-- TOC entry 219 (class 1259 OID 11340823)
-- Name: user_profile; Type: TABLE; Schema: public; Owner: siette; Tablespace: 
--

CREATE TABLE user_profile (
    id bigint NOT NULL,
    code character varying(255) NOT NULL,
    description text,
    name character varying(255) NOT NULL
);


ALTER TABLE public.user_profile OWNER TO siette;

--
-- TOC entry 234 (class 1259 OID 11340910)
-- Name: user_profile_permission; Type: TABLE; Schema: public; Owner: siette; Tablespace: 
--

CREATE TABLE user_profile_permission (
    user_profile_id bigint NOT NULL,
    permission_id bigint NOT NULL
);


ALTER TABLE public.user_profile_permission OWNER TO siette;

--
-- TOC entry 220 (class 1259 OID 11340835)
-- Name: user_role; Type: TABLE; Schema: public; Owner: siette; Tablespace: 
--

CREATE TABLE user_role (
    user_id bigint NOT NULL,
    role_id bigint NOT NULL
);


ALTER TABLE public.user_role OWNER TO siette;

--
-- TOC entry 221 (class 1259 OID 11340840)
-- Name: user_user_profile; Type: TABLE; Schema: public; Owner: siette; Tablespace: 
--

CREATE TABLE user_user_profile (
    user_id bigint NOT NULL,
    user_profile_id bigint NOT NULL
);


ALTER TABLE public.user_user_profile OWNER TO siette;

--
-- TOC entry 222 (class 1259 OID 11340845)
-- Name: week_day; Type: TABLE; Schema: public; Owner: siette; Tablespace: 
--

CREATE TABLE week_day (
    id bigint NOT NULL,
    code character varying(255) NOT NULL,
    name character varying(255) NOT NULL
);


ALTER TABLE public.week_day OWNER TO siette;

--
-- TOC entry 2510 (class 0 OID 11340421)
-- Dependencies: 168
-- Data for Name: academic_level; Type: TABLE DATA; Schema: public; Owner: siette
--



--
-- TOC entry 2511 (class 0 OID 11340431)
-- Dependencies: 169
-- Data for Name: academic_period; Type: TABLE DATA; Schema: public; Owner: siette
--



--
-- TOC entry 2512 (class 0 OID 11340443)
-- Dependencies: 170
-- Data for Name: address; Type: TABLE DATA; Schema: public; Owner: siette
--



--
-- TOC entry 2513 (class 0 OID 11340451)
-- Dependencies: 171
-- Data for Name: available_state; Type: TABLE DATA; Schema: public; Owner: siette
--



--
-- TOC entry 2514 (class 0 OID 11340456)
-- Dependencies: 172
-- Data for Name: bank; Type: TABLE DATA; Schema: public; Owner: siette
--



--
-- TOC entry 2515 (class 0 OID 11340466)
-- Dependencies: 173
-- Data for Name: cap; Type: TABLE DATA; Schema: public; Owner: siette
--



--
-- TOC entry 2516 (class 0 OID 11340482)
-- Dependencies: 174
-- Data for Name: center; Type: TABLE DATA; Schema: public; Owner: siette
--



--
-- TOC entry 2517 (class 0 OID 11340492)
-- Dependencies: 175
-- Data for Name: classroom; Type: TABLE DATA; Schema: public; Owner: siette
--



--
-- TOC entry 2518 (class 0 OID 11340500)
-- Dependencies: 176
-- Data for Name: classroom_time_block; Type: TABLE DATA; Schema: public; Owner: siette
--



--
-- TOC entry 2565 (class 0 OID 11340855)
-- Dependencies: 223
-- Data for Name: classroom_time_block_student_type; Type: TABLE DATA; Schema: public; Owner: siette
--



--
-- TOC entry 2519 (class 0 OID 11340508)
-- Dependencies: 177
-- Data for Name: component; Type: TABLE DATA; Schema: public; Owner: siette
--



--
-- TOC entry 2520 (class 0 OID 11340515)
-- Dependencies: 178
-- Data for Name: department; Type: TABLE DATA; Schema: public; Owner: siette
--



--
-- TOC entry 2521 (class 0 OID 11340523)
-- Dependencies: 179
-- Data for Name: doc_file; Type: TABLE DATA; Schema: public; Owner: siette
--



--
-- TOC entry 2522 (class 0 OID 11340531)
-- Dependencies: 180
-- Data for Name: email_notifications; Type: TABLE DATA; Schema: public; Owner: siette
--



--
-- TOC entry 2523 (class 0 OID 11340536)
-- Dependencies: 181
-- Data for Name: evaluation_assignment; Type: TABLE DATA; Schema: public; Owner: siette
--



--
-- TOC entry 2524 (class 0 OID 11340543)
-- Dependencies: 182
-- Data for Name: evaluation_assignment_matter; Type: TABLE DATA; Schema: public; Owner: siette
--



--
-- TOC entry 2525 (class 0 OID 11340551)
-- Dependencies: 183
-- Data for Name: evaluation_center; Type: TABLE DATA; Schema: public; Owner: siette
--



--
-- TOC entry 2526 (class 0 OID 11340563)
-- Dependencies: 184
-- Data for Name: evaluation_event; Type: TABLE DATA; Schema: public; Owner: siette
--



--
-- TOC entry 2566 (class 0 OID 11340860)
-- Dependencies: 224
-- Data for Name: evaluation_event_admin; Type: TABLE DATA; Schema: public; Owner: siette
--



--
-- TOC entry 2527 (class 0 OID 11340573)
-- Dependencies: 185
-- Data for Name: evaluation_event_classroom; Type: TABLE DATA; Schema: public; Owner: siette
--



--
-- TOC entry 2528 (class 0 OID 11340578)
-- Dependencies: 186
-- Data for Name: evaluation_event_configuration; Type: TABLE DATA; Schema: public; Owner: siette
--



--
-- TOC entry 2529 (class 0 OID 11340583)
-- Dependencies: 187
-- Data for Name: evaluation_event_evaluation_center; Type: TABLE DATA; Schema: public; Owner: siette
--



--
-- TOC entry 2530 (class 0 OID 11340588)
-- Dependencies: 188
-- Data for Name: evaluation_event_evaluation_center_center; Type: TABLE DATA; Schema: public; Owner: siette
--



--
-- TOC entry 2567 (class 0 OID 11340865)
-- Dependencies: 225
-- Data for Name: evaluation_event_evaluation_type; Type: TABLE DATA; Schema: public; Owner: siette
--



--
-- TOC entry 2531 (class 0 OID 11340595)
-- Dependencies: 189
-- Data for Name: evaluation_event_matter; Type: TABLE DATA; Schema: public; Owner: siette
--



--
-- TOC entry 2568 (class 0 OID 11340870)
-- Dependencies: 226
-- Data for Name: evaluation_event_matter_day; Type: TABLE DATA; Schema: public; Owner: siette
--



--
-- TOC entry 2569 (class 0 OID 11340875)
-- Dependencies: 227
-- Data for Name: evaluation_event_matter_evaluation_type; Type: TABLE DATA; Schema: public; Owner: siette
--



--
-- TOC entry 2532 (class 0 OID 11340602)
-- Dependencies: 190
-- Data for Name: evaluation_event_matter_test; Type: TABLE DATA; Schema: public; Owner: siette
--



--
-- TOC entry 2533 (class 0 OID 11340609)
-- Dependencies: 191
-- Data for Name: evaluation_event_teacher; Type: TABLE DATA; Schema: public; Owner: siette
--



--
-- TOC entry 2570 (class 0 OID 11340880)
-- Dependencies: 228
-- Data for Name: evaluation_event_teacher_classroom_time_block; Type: TABLE DATA; Schema: public; Owner: siette
--



--
-- TOC entry 2534 (class 0 OID 11340614)
-- Dependencies: 192
-- Data for Name: evaluation_type; Type: TABLE DATA; Schema: public; Owner: siette
--



--
-- TOC entry 2535 (class 0 OID 11340624)
-- Dependencies: 193
-- Data for Name: evs_user; Type: TABLE DATA; Schema: public; Owner: siette
--



--
-- TOC entry 2536 (class 0 OID 11340634)
-- Dependencies: 194
-- Data for Name: generic_schedule_log; Type: TABLE DATA; Schema: public; Owner: siette
--



--
-- TOC entry 2586 (class 0 OID 0)
-- Dependencies: 235
-- Name: hibernate_sequence; Type: SEQUENCE SET; Schema: public; Owner: siette
--

SELECT pg_catalog.setval('hibernate_sequence', 1, false);


--
-- TOC entry 2537 (class 0 OID 11340642)
-- Dependencies: 195
-- Data for Name: matter; Type: TABLE DATA; Schema: public; Owner: siette
--



--
-- TOC entry 2538 (class 0 OID 11340652)
-- Dependencies: 196
-- Data for Name: matter_bank; Type: TABLE DATA; Schema: public; Owner: siette
--



--
-- TOC entry 2571 (class 0 OID 11340885)
-- Dependencies: 229
-- Data for Name: matter_day; Type: TABLE DATA; Schema: public; Owner: siette
--



--
-- TOC entry 2539 (class 0 OID 11340657)
-- Dependencies: 197
-- Data for Name: matter_subject; Type: TABLE DATA; Schema: public; Owner: siette
--



--
-- TOC entry 2540 (class 0 OID 11340662)
-- Dependencies: 198
-- Data for Name: matter_test_student; Type: TABLE DATA; Schema: public; Owner: siette
--



--
-- TOC entry 2541 (class 0 OID 11340669)
-- Dependencies: 199
-- Data for Name: mode; Type: TABLE DATA; Schema: public; Owner: siette
--



--
-- TOC entry 2542 (class 0 OID 11340679)
-- Dependencies: 200
-- Data for Name: net; Type: TABLE DATA; Schema: public; Owner: siette
--



--
-- TOC entry 2572 (class 0 OID 11340890)
-- Dependencies: 230
-- Data for Name: net_server; Type: TABLE DATA; Schema: public; Owner: siette
--



--
-- TOC entry 2543 (class 0 OID 11340689)
-- Dependencies: 201
-- Data for Name: permission; Type: TABLE DATA; Schema: public; Owner: siette
--



--
-- TOC entry 2544 (class 0 OID 11340697)
-- Dependencies: 202
-- Data for Name: registration; Type: TABLE DATA; Schema: public; Owner: siette
--



--
-- TOC entry 2573 (class 0 OID 11340895)
-- Dependencies: 231
-- Data for Name: registration_subject; Type: TABLE DATA; Schema: public; Owner: siette
--



--
-- TOC entry 2545 (class 0 OID 11340704)
-- Dependencies: 203
-- Data for Name: role; Type: TABLE DATA; Schema: public; Owner: siette
--



--
-- TOC entry 2546 (class 0 OID 11340712)
-- Dependencies: 204
-- Data for Name: schedule; Type: TABLE DATA; Schema: public; Owner: siette
--



--
-- TOC entry 2547 (class 0 OID 11340717)
-- Dependencies: 205
-- Data for Name: schedule_modification; Type: TABLE DATA; Schema: public; Owner: siette
--



--
-- TOC entry 2548 (class 0 OID 11340725)
-- Dependencies: 206
-- Data for Name: schedule_modification_date; Type: TABLE DATA; Schema: public; Owner: siette
--



--
-- TOC entry 2549 (class 0 OID 11340730)
-- Dependencies: 207
-- Data for Name: schedule_modification_info; Type: TABLE DATA; Schema: public; Owner: siette
--



--
-- TOC entry 2550 (class 0 OID 11340735)
-- Dependencies: 208
-- Data for Name: server; Type: TABLE DATA; Schema: public; Owner: siette
--



--
-- TOC entry 2551 (class 0 OID 11340745)
-- Dependencies: 209
-- Data for Name: session; Type: TABLE DATA; Schema: public; Owner: siette
--



--
-- TOC entry 2552 (class 0 OID 11340753)
-- Dependencies: 210
-- Data for Name: student_evaluation; Type: TABLE DATA; Schema: public; Owner: siette
--



--
-- TOC entry 2553 (class 0 OID 11340761)
-- Dependencies: 211
-- Data for Name: student_test_schedule_log; Type: TABLE DATA; Schema: public; Owner: siette
--



--
-- TOC entry 2554 (class 0 OID 11340769)
-- Dependencies: 212
-- Data for Name: student_type; Type: TABLE DATA; Schema: public; Owner: siette
--



--
-- TOC entry 2555 (class 0 OID 11340776)
-- Dependencies: 213
-- Data for Name: subject; Type: TABLE DATA; Schema: public; Owner: siette
--



--
-- TOC entry 2556 (class 0 OID 11340784)
-- Dependencies: 214
-- Data for Name: test; Type: TABLE DATA; Schema: public; Owner: siette
--



--
-- TOC entry 2557 (class 0 OID 11340794)
-- Dependencies: 215
-- Data for Name: test_sync; Type: TABLE DATA; Schema: public; Owner: siette
--



--
-- TOC entry 2558 (class 0 OID 11340802)
-- Dependencies: 216
-- Data for Name: test_type; Type: TABLE DATA; Schema: public; Owner: siette
--



--
-- TOC entry 2559 (class 0 OID 11340810)
-- Dependencies: 217
-- Data for Name: time_block; Type: TABLE DATA; Schema: public; Owner: siette
--



--
-- TOC entry 2574 (class 0 OID 11340900)
-- Dependencies: 232
-- Data for Name: time_block_student_type; Type: TABLE DATA; Schema: public; Owner: siette
--



--
-- TOC entry 2560 (class 0 OID 11340815)
-- Dependencies: 218
-- Data for Name: transport; Type: TABLE DATA; Schema: public; Owner: siette
--



--
-- TOC entry 2575 (class 0 OID 11340905)
-- Dependencies: 233
-- Data for Name: user_permission; Type: TABLE DATA; Schema: public; Owner: siette
--



--
-- TOC entry 2561 (class 0 OID 11340823)
-- Dependencies: 219
-- Data for Name: user_profile; Type: TABLE DATA; Schema: public; Owner: siette
--



--
-- TOC entry 2576 (class 0 OID 11340910)
-- Dependencies: 234
-- Data for Name: user_profile_permission; Type: TABLE DATA; Schema: public; Owner: siette
--



--
-- TOC entry 2562 (class 0 OID 11340835)
-- Dependencies: 220
-- Data for Name: user_role; Type: TABLE DATA; Schema: public; Owner: siette
--



--
-- TOC entry 2563 (class 0 OID 11340840)
-- Dependencies: 221
-- Data for Name: user_user_profile; Type: TABLE DATA; Schema: public; Owner: siette
--



--
-- TOC entry 2564 (class 0 OID 11340845)
-- Dependencies: 222
-- Data for Name: week_day; Type: TABLE DATA; Schema: public; Owner: siette
--



--
-- TOC entry 2214 (class 2606 OID 11340430)
-- Name: academic_level_code_key; Type: CONSTRAINT; Schema: public; Owner: siette; Tablespace: 
--

ALTER TABLE ONLY academic_level
    ADD CONSTRAINT academic_level_code_key UNIQUE (code);


--
-- TOC entry 2216 (class 2606 OID 11340428)
-- Name: academic_level_pkey; Type: CONSTRAINT; Schema: public; Owner: siette; Tablespace: 
--

ALTER TABLE ONLY academic_level
    ADD CONSTRAINT academic_level_pkey PRIMARY KEY (id);


--
-- TOC entry 2218 (class 2606 OID 11340440)
-- Name: academic_period_code_key; Type: CONSTRAINT; Schema: public; Owner: siette; Tablespace: 
--

ALTER TABLE ONLY academic_period
    ADD CONSTRAINT academic_period_code_key UNIQUE (code);


--
-- TOC entry 2220 (class 2606 OID 11340442)
-- Name: academic_period_externalid_key; Type: CONSTRAINT; Schema: public; Owner: siette; Tablespace: 
--

ALTER TABLE ONLY academic_period
    ADD CONSTRAINT academic_period_externalid_key UNIQUE (externalid);


--
-- TOC entry 2222 (class 2606 OID 11340438)
-- Name: academic_period_pkey; Type: CONSTRAINT; Schema: public; Owner: siette; Tablespace: 
--

ALTER TABLE ONLY academic_period
    ADD CONSTRAINT academic_period_pkey PRIMARY KEY (id);


--
-- TOC entry 2224 (class 2606 OID 11340450)
-- Name: address_pkey; Type: CONSTRAINT; Schema: public; Owner: siette; Tablespace: 
--

ALTER TABLE ONLY address
    ADD CONSTRAINT address_pkey PRIMARY KEY (id);


--
-- TOC entry 2226 (class 2606 OID 11340455)
-- Name: available_state_pkey; Type: CONSTRAINT; Schema: public; Owner: siette; Tablespace: 
--

ALTER TABLE ONLY available_state
    ADD CONSTRAINT available_state_pkey PRIMARY KEY (id);


--
-- TOC entry 2228 (class 2606 OID 11340465)
-- Name: bank_externalid_key; Type: CONSTRAINT; Schema: public; Owner: siette; Tablespace: 
--

ALTER TABLE ONLY bank
    ADD CONSTRAINT bank_externalid_key UNIQUE (externalid);


--
-- TOC entry 2230 (class 2606 OID 11340463)
-- Name: bank_pkey; Type: CONSTRAINT; Schema: public; Owner: siette; Tablespace: 
--

ALTER TABLE ONLY bank
    ADD CONSTRAINT bank_pkey PRIMARY KEY (id);


--
-- TOC entry 2232 (class 2606 OID 11340475)
-- Name: cap_key_key; Type: CONSTRAINT; Schema: public; Owner: siette; Tablespace: 
--

ALTER TABLE ONLY cap
    ADD CONSTRAINT cap_key_key UNIQUE (key);


--
-- TOC entry 2234 (class 2606 OID 11340477)
-- Name: cap_name_key; Type: CONSTRAINT; Schema: public; Owner: siette; Tablespace: 
--

ALTER TABLE ONLY cap
    ADD CONSTRAINT cap_name_key UNIQUE (name);


--
-- TOC entry 2236 (class 2606 OID 11340473)
-- Name: cap_pkey; Type: CONSTRAINT; Schema: public; Owner: siette; Tablespace: 
--

ALTER TABLE ONLY cap
    ADD CONSTRAINT cap_pkey PRIMARY KEY (id);


--
-- TOC entry 2238 (class 2606 OID 11340479)
-- Name: cap_serialnumber_key; Type: CONSTRAINT; Schema: public; Owner: siette; Tablespace: 
--

ALTER TABLE ONLY cap
    ADD CONSTRAINT cap_serialnumber_key UNIQUE (serialnumber);


--
-- TOC entry 2240 (class 2606 OID 11340481)
-- Name: cap_ssid_key; Type: CONSTRAINT; Schema: public; Owner: siette; Tablespace: 
--

ALTER TABLE ONLY cap
    ADD CONSTRAINT cap_ssid_key UNIQUE (ssid);


--
-- TOC entry 2242 (class 2606 OID 11340491)
-- Name: center_code_key; Type: CONSTRAINT; Schema: public; Owner: siette; Tablespace: 
--

ALTER TABLE ONLY center
    ADD CONSTRAINT center_code_key UNIQUE (code);


--
-- TOC entry 2244 (class 2606 OID 11340489)
-- Name: center_pkey; Type: CONSTRAINT; Schema: public; Owner: siette; Tablespace: 
--

ALTER TABLE ONLY center
    ADD CONSTRAINT center_pkey PRIMARY KEY (id);


--
-- TOC entry 2246 (class 2606 OID 11340499)
-- Name: classroom_pkey; Type: CONSTRAINT; Schema: public; Owner: siette; Tablespace: 
--

ALTER TABLE ONLY classroom
    ADD CONSTRAINT classroom_pkey PRIMARY KEY (id);


--
-- TOC entry 2248 (class 2606 OID 11340507)
-- Name: classroom_time_block_pkey; Type: CONSTRAINT; Schema: public; Owner: siette; Tablespace: 
--

ALTER TABLE ONLY classroom_time_block
    ADD CONSTRAINT classroom_time_block_pkey PRIMARY KEY (id);


--
-- TOC entry 2384 (class 2606 OID 11340859)
-- Name: classroom_time_block_student_type_pkey; Type: CONSTRAINT; Schema: public; Owner: siette; Tablespace: 
--

ALTER TABLE ONLY classroom_time_block_student_type
    ADD CONSTRAINT classroom_time_block_student_type_pkey PRIMARY KEY (classroom_time_block_id, student_type_id);


--
-- TOC entry 2250 (class 2606 OID 11340514)
-- Name: component_code_key; Type: CONSTRAINT; Schema: public; Owner: siette; Tablespace: 
--

ALTER TABLE ONLY component
    ADD CONSTRAINT component_code_key UNIQUE (code);


--
-- TOC entry 2252 (class 2606 OID 11340512)
-- Name: component_pkey; Type: CONSTRAINT; Schema: public; Owner: siette; Tablespace: 
--

ALTER TABLE ONLY component
    ADD CONSTRAINT component_pkey PRIMARY KEY (id);


--
-- TOC entry 2254 (class 2606 OID 11340522)
-- Name: department_pkey; Type: CONSTRAINT; Schema: public; Owner: siette; Tablespace: 
--

ALTER TABLE ONLY department
    ADD CONSTRAINT department_pkey PRIMARY KEY (id);


--
-- TOC entry 2256 (class 2606 OID 11340530)
-- Name: doc_file_pkey; Type: CONSTRAINT; Schema: public; Owner: siette; Tablespace: 
--

ALTER TABLE ONLY doc_file
    ADD CONSTRAINT doc_file_pkey PRIMARY KEY (id);


--
-- TOC entry 2258 (class 2606 OID 11340535)
-- Name: email_notifications_pkey; Type: CONSTRAINT; Schema: public; Owner: siette; Tablespace: 
--

ALTER TABLE ONLY email_notifications
    ADD CONSTRAINT email_notifications_pkey PRIMARY KEY (id);


--
-- TOC entry 2260 (class 2606 OID 11340542)
-- Name: evaluation_assignment_evaluationevent_id_user_id_key; Type: CONSTRAINT; Schema: public; Owner: siette; Tablespace: 
--

ALTER TABLE ONLY evaluation_assignment
    ADD CONSTRAINT evaluation_assignment_evaluationevent_id_user_id_key UNIQUE (evaluationevent_id, user_id);


--
-- TOC entry 2264 (class 2606 OID 11340550)
-- Name: evaluation_assignment_matter_pkey; Type: CONSTRAINT; Schema: public; Owner: siette; Tablespace: 
--

ALTER TABLE ONLY evaluation_assignment_matter
    ADD CONSTRAINT evaluation_assignment_matter_pkey PRIMARY KEY (id);


--
-- TOC entry 2262 (class 2606 OID 11340540)
-- Name: evaluation_assignment_pkey; Type: CONSTRAINT; Schema: public; Owner: siette; Tablespace: 
--

ALTER TABLE ONLY evaluation_assignment
    ADD CONSTRAINT evaluation_assignment_pkey PRIMARY KEY (id);


--
-- TOC entry 2266 (class 2606 OID 11340560)
-- Name: evaluation_center_code_key; Type: CONSTRAINT; Schema: public; Owner: siette; Tablespace: 
--

ALTER TABLE ONLY evaluation_center
    ADD CONSTRAINT evaluation_center_code_key UNIQUE (code);


--
-- TOC entry 2268 (class 2606 OID 11340562)
-- Name: evaluation_center_name_key; Type: CONSTRAINT; Schema: public; Owner: siette; Tablespace: 
--

ALTER TABLE ONLY evaluation_center
    ADD CONSTRAINT evaluation_center_name_key UNIQUE (name);


--
-- TOC entry 2270 (class 2606 OID 11340558)
-- Name: evaluation_center_pkey; Type: CONSTRAINT; Schema: public; Owner: siette; Tablespace: 
--

ALTER TABLE ONLY evaluation_center
    ADD CONSTRAINT evaluation_center_pkey PRIMARY KEY (id);


--
-- TOC entry 2386 (class 2606 OID 11340864)
-- Name: evaluation_event_admin_pkey; Type: CONSTRAINT; Schema: public; Owner: siette; Tablespace: 
--

ALTER TABLE ONLY evaluation_event_admin
    ADD CONSTRAINT evaluation_event_admin_pkey PRIMARY KEY (evaluation_event_id, user_id);


--
-- TOC entry 2276 (class 2606 OID 11340577)
-- Name: evaluation_event_classroom_pkey; Type: CONSTRAINT; Schema: public; Owner: siette; Tablespace: 
--

ALTER TABLE ONLY evaluation_event_classroom
    ADD CONSTRAINT evaluation_event_classroom_pkey PRIMARY KEY (id);


--
-- TOC entry 2272 (class 2606 OID 11340572)
-- Name: evaluation_event_code_key; Type: CONSTRAINT; Schema: public; Owner: siette; Tablespace: 
--

ALTER TABLE ONLY evaluation_event
    ADD CONSTRAINT evaluation_event_code_key UNIQUE (code);


--
-- TOC entry 2278 (class 2606 OID 11340582)
-- Name: evaluation_event_configuration_pkey; Type: CONSTRAINT; Schema: public; Owner: siette; Tablespace: 
--

ALTER TABLE ONLY evaluation_event_configuration
    ADD CONSTRAINT evaluation_event_configuration_pkey PRIMARY KEY (id);


--
-- TOC entry 2282 (class 2606 OID 11340594)
-- Name: evaluation_event_evaluation_c_center_id_evaluation_event_ev_key; Type: CONSTRAINT; Schema: public; Owner: siette; Tablespace: 
--

ALTER TABLE ONLY evaluation_event_evaluation_center_center
    ADD CONSTRAINT evaluation_event_evaluation_c_center_id_evaluation_event_ev_key UNIQUE (center_id, evaluation_event_evaluation_center_id);


--
-- TOC entry 2284 (class 2606 OID 11340592)
-- Name: evaluation_event_evaluation_center_center_pkey; Type: CONSTRAINT; Schema: public; Owner: siette; Tablespace: 
--

ALTER TABLE ONLY evaluation_event_evaluation_center_center
    ADD CONSTRAINT evaluation_event_evaluation_center_center_pkey PRIMARY KEY (id);


--
-- TOC entry 2280 (class 2606 OID 11340587)
-- Name: evaluation_event_evaluation_center_pkey; Type: CONSTRAINT; Schema: public; Owner: siette; Tablespace: 
--

ALTER TABLE ONLY evaluation_event_evaluation_center
    ADD CONSTRAINT evaluation_event_evaluation_center_pkey PRIMARY KEY (id);


--
-- TOC entry 2388 (class 2606 OID 11340869)
-- Name: evaluation_event_evaluation_type_pkey; Type: CONSTRAINT; Schema: public; Owner: siette; Tablespace: 
--

ALTER TABLE ONLY evaluation_event_evaluation_type
    ADD CONSTRAINT evaluation_event_evaluation_type_pkey PRIMARY KEY (evaluation_event_id, evaluation_type_id);


--
-- TOC entry 2390 (class 2606 OID 11340874)
-- Name: evaluation_event_matter_day_pkey; Type: CONSTRAINT; Schema: public; Owner: siette; Tablespace: 
--

ALTER TABLE ONLY evaluation_event_matter_day
    ADD CONSTRAINT evaluation_event_matter_day_pkey PRIMARY KEY (evaluationeventmatter_id, daysallowed_id);


--
-- TOC entry 2392 (class 2606 OID 11340879)
-- Name: evaluation_event_matter_evaluation_type_pkey; Type: CONSTRAINT; Schema: public; Owner: siette; Tablespace: 
--

ALTER TABLE ONLY evaluation_event_matter_evaluation_type
    ADD CONSTRAINT evaluation_event_matter_evaluation_type_pkey PRIMARY KEY (evaluationeventmatter_id, evaluation_type_id);


--
-- TOC entry 2286 (class 2606 OID 11340601)
-- Name: evaluation_event_matter_evaluationevent_id_matter_id_academ_key; Type: CONSTRAINT; Schema: public; Owner: siette; Tablespace: 
--

ALTER TABLE ONLY evaluation_event_matter
    ADD CONSTRAINT evaluation_event_matter_evaluationevent_id_matter_id_academ_key UNIQUE (evaluationevent_id, matter_id, academicperiod_id, mode_id);


--
-- TOC entry 2288 (class 2606 OID 11340599)
-- Name: evaluation_event_matter_pkey; Type: CONSTRAINT; Schema: public; Owner: siette; Tablespace: 
--

ALTER TABLE ONLY evaluation_event_matter
    ADD CONSTRAINT evaluation_event_matter_pkey PRIMARY KEY (id);


--
-- TOC entry 2290 (class 2606 OID 11340606)
-- Name: evaluation_event_matter_test_pkey; Type: CONSTRAINT; Schema: public; Owner: siette; Tablespace: 
--

ALTER TABLE ONLY evaluation_event_matter_test
    ADD CONSTRAINT evaluation_event_matter_test_pkey PRIMARY KEY (id);


--
-- TOC entry 2292 (class 2606 OID 11340608)
-- Name: evaluation_event_matter_test_test_id_evaluationeventmatter__key; Type: CONSTRAINT; Schema: public; Owner: siette; Tablespace: 
--

ALTER TABLE ONLY evaluation_event_matter_test
    ADD CONSTRAINT evaluation_event_matter_test_test_id_evaluationeventmatter__key UNIQUE (test_id, evaluationeventmatter_id);


--
-- TOC entry 2274 (class 2606 OID 11340570)
-- Name: evaluation_event_pkey; Type: CONSTRAINT; Schema: public; Owner: siette; Tablespace: 
--

ALTER TABLE ONLY evaluation_event
    ADD CONSTRAINT evaluation_event_pkey PRIMARY KEY (id);


--
-- TOC entry 2394 (class 2606 OID 11340884)
-- Name: evaluation_event_teacher_classroom_time_block_pkey; Type: CONSTRAINT; Schema: public; Owner: siette; Tablespace: 
--

ALTER TABLE ONLY evaluation_event_teacher_classroom_time_block
    ADD CONSTRAINT evaluation_event_teacher_classroom_time_block_pkey PRIMARY KEY (classroom_time_block_id, evaluation_event_teacher_id);


--
-- TOC entry 2294 (class 2606 OID 11340613)
-- Name: evaluation_event_teacher_pkey; Type: CONSTRAINT; Schema: public; Owner: siette; Tablespace: 
--

ALTER TABLE ONLY evaluation_event_teacher
    ADD CONSTRAINT evaluation_event_teacher_pkey PRIMARY KEY (id);


--
-- TOC entry 2296 (class 2606 OID 11340623)
-- Name: evaluation_type_code_key; Type: CONSTRAINT; Schema: public; Owner: siette; Tablespace: 
--

ALTER TABLE ONLY evaluation_type
    ADD CONSTRAINT evaluation_type_code_key UNIQUE (code);


--
-- TOC entry 2298 (class 2606 OID 11340621)
-- Name: evaluation_type_pkey; Type: CONSTRAINT; Schema: public; Owner: siette; Tablespace: 
--

ALTER TABLE ONLY evaluation_type
    ADD CONSTRAINT evaluation_type_pkey PRIMARY KEY (id);


--
-- TOC entry 2300 (class 2606 OID 11340631)
-- Name: evs_user_pkey; Type: CONSTRAINT; Schema: public; Owner: siette; Tablespace: 
--

ALTER TABLE ONLY evs_user
    ADD CONSTRAINT evs_user_pkey PRIMARY KEY (id);


--
-- TOC entry 2302 (class 2606 OID 11340633)
-- Name: evs_user_username_key; Type: CONSTRAINT; Schema: public; Owner: siette; Tablespace: 
--

ALTER TABLE ONLY evs_user
    ADD CONSTRAINT evs_user_username_key UNIQUE (username);


--
-- TOC entry 2304 (class 2606 OID 11340641)
-- Name: generic_schedule_log_pkey; Type: CONSTRAINT; Schema: public; Owner: siette; Tablespace: 
--

ALTER TABLE ONLY generic_schedule_log
    ADD CONSTRAINT generic_schedule_log_pkey PRIMARY KEY (id);


--
-- TOC entry 2310 (class 2606 OID 11340656)
-- Name: matter_bank_pkey; Type: CONSTRAINT; Schema: public; Owner: siette; Tablespace: 
--

ALTER TABLE ONLY matter_bank
    ADD CONSTRAINT matter_bank_pkey PRIMARY KEY (matter_id, bank_id);


--
-- TOC entry 2306 (class 2606 OID 11340651)
-- Name: matter_code_key; Type: CONSTRAINT; Schema: public; Owner: siette; Tablespace: 
--

ALTER TABLE ONLY matter
    ADD CONSTRAINT matter_code_key UNIQUE (code);


--
-- TOC entry 2396 (class 2606 OID 11340889)
-- Name: matter_day_pkey; Type: CONSTRAINT; Schema: public; Owner: siette; Tablespace: 
--

ALTER TABLE ONLY matter_day
    ADD CONSTRAINT matter_day_pkey PRIMARY KEY (matter_id, daysallowed_id);


--
-- TOC entry 2308 (class 2606 OID 11340649)
-- Name: matter_pkey; Type: CONSTRAINT; Schema: public; Owner: siette; Tablespace: 
--

ALTER TABLE ONLY matter
    ADD CONSTRAINT matter_pkey PRIMARY KEY (id);


--
-- TOC entry 2312 (class 2606 OID 11340661)
-- Name: matter_subject_pkey; Type: CONSTRAINT; Schema: public; Owner: siette; Tablespace: 
--

ALTER TABLE ONLY matter_subject
    ADD CONSTRAINT matter_subject_pkey PRIMARY KEY (matter_id, subject_id);


--
-- TOC entry 2314 (class 2606 OID 11340668)
-- Name: matter_test_student_evaluationeventmattertest_id_evaluation_key; Type: CONSTRAINT; Schema: public; Owner: siette; Tablespace: 
--

ALTER TABLE ONLY matter_test_student
    ADD CONSTRAINT matter_test_student_evaluationeventmattertest_id_evaluation_key UNIQUE (evaluationeventmattertest_id, evaluationassignmentmatter_id);


--
-- TOC entry 2316 (class 2606 OID 11340666)
-- Name: matter_test_student_pkey; Type: CONSTRAINT; Schema: public; Owner: siette; Tablespace: 
--

ALTER TABLE ONLY matter_test_student
    ADD CONSTRAINT matter_test_student_pkey PRIMARY KEY (id);


--
-- TOC entry 2318 (class 2606 OID 11340678)
-- Name: mode_code_key; Type: CONSTRAINT; Schema: public; Owner: siette; Tablespace: 
--

ALTER TABLE ONLY mode
    ADD CONSTRAINT mode_code_key UNIQUE (code);


--
-- TOC entry 2320 (class 2606 OID 11340676)
-- Name: mode_pkey; Type: CONSTRAINT; Schema: public; Owner: siette; Tablespace: 
--

ALTER TABLE ONLY mode
    ADD CONSTRAINT mode_pkey PRIMARY KEY (id);


--
-- TOC entry 2322 (class 2606 OID 11340688)
-- Name: net_code_key; Type: CONSTRAINT; Schema: public; Owner: siette; Tablespace: 
--

ALTER TABLE ONLY net
    ADD CONSTRAINT net_code_key UNIQUE (code);


--
-- TOC entry 2324 (class 2606 OID 11340686)
-- Name: net_pkey; Type: CONSTRAINT; Schema: public; Owner: siette; Tablespace: 
--

ALTER TABLE ONLY net
    ADD CONSTRAINT net_pkey PRIMARY KEY (id);


--
-- TOC entry 2398 (class 2606 OID 11340894)
-- Name: net_server_pkey; Type: CONSTRAINT; Schema: public; Owner: siette; Tablespace: 
--

ALTER TABLE ONLY net_server
    ADD CONSTRAINT net_server_pkey PRIMARY KEY (net_id_id, server_id);


--
-- TOC entry 2326 (class 2606 OID 11340696)
-- Name: permission_pkey; Type: CONSTRAINT; Schema: public; Owner: siette; Tablespace: 
--

ALTER TABLE ONLY permission
    ADD CONSTRAINT permission_pkey PRIMARY KEY (id);


--
-- TOC entry 2328 (class 2606 OID 11340703)
-- Name: registration_center_id_user_id_key; Type: CONSTRAINT; Schema: public; Owner: siette; Tablespace: 
--

ALTER TABLE ONLY registration
    ADD CONSTRAINT registration_center_id_user_id_key UNIQUE (center_id, user_id);


--
-- TOC entry 2330 (class 2606 OID 11340701)
-- Name: registration_pkey; Type: CONSTRAINT; Schema: public; Owner: siette; Tablespace: 
--

ALTER TABLE ONLY registration
    ADD CONSTRAINT registration_pkey PRIMARY KEY (id);


--
-- TOC entry 2400 (class 2606 OID 11340899)
-- Name: registration_subject_pkey; Type: CONSTRAINT; Schema: public; Owner: siette; Tablespace: 
--

ALTER TABLE ONLY registration_subject
    ADD CONSTRAINT registration_subject_pkey PRIMARY KEY (registration_id, subject_id);


--
-- TOC entry 2332 (class 2606 OID 11340711)
-- Name: role_pkey; Type: CONSTRAINT; Schema: public; Owner: siette; Tablespace: 
--

ALTER TABLE ONLY role
    ADD CONSTRAINT role_pkey PRIMARY KEY (id);


--
-- TOC entry 2338 (class 2606 OID 11340729)
-- Name: schedule_modification_date_pkey; Type: CONSTRAINT; Schema: public; Owner: siette; Tablespace: 
--

ALTER TABLE ONLY schedule_modification_date
    ADD CONSTRAINT schedule_modification_date_pkey PRIMARY KEY (id);


--
-- TOC entry 2340 (class 2606 OID 11340734)
-- Name: schedule_modification_info_pkey; Type: CONSTRAINT; Schema: public; Owner: siette; Tablespace: 
--

ALTER TABLE ONLY schedule_modification_info
    ADD CONSTRAINT schedule_modification_info_pkey PRIMARY KEY (id);


--
-- TOC entry 2336 (class 2606 OID 11340724)
-- Name: schedule_modification_pkey; Type: CONSTRAINT; Schema: public; Owner: siette; Tablespace: 
--

ALTER TABLE ONLY schedule_modification
    ADD CONSTRAINT schedule_modification_pkey PRIMARY KEY (id);


--
-- TOC entry 2334 (class 2606 OID 11340716)
-- Name: schedule_pkey; Type: CONSTRAINT; Schema: public; Owner: siette; Tablespace: 
--

ALTER TABLE ONLY schedule
    ADD CONSTRAINT schedule_pkey PRIMARY KEY (id);


--
-- TOC entry 2342 (class 2606 OID 11340744)
-- Name: server_code_key; Type: CONSTRAINT; Schema: public; Owner: siette; Tablespace: 
--

ALTER TABLE ONLY server
    ADD CONSTRAINT server_code_key UNIQUE (code);


--
-- TOC entry 2344 (class 2606 OID 11340742)
-- Name: server_pkey; Type: CONSTRAINT; Schema: public; Owner: siette; Tablespace: 
--

ALTER TABLE ONLY server
    ADD CONSTRAINT server_pkey PRIMARY KEY (id);


--
-- TOC entry 2346 (class 2606 OID 11340752)
-- Name: session_pkey; Type: CONSTRAINT; Schema: public; Owner: siette; Tablespace: 
--

ALTER TABLE ONLY session
    ADD CONSTRAINT session_pkey PRIMARY KEY (id);


--
-- TOC entry 2348 (class 2606 OID 11340760)
-- Name: student_evaluation_pkey; Type: CONSTRAINT; Schema: public; Owner: siette; Tablespace: 
--

ALTER TABLE ONLY student_evaluation
    ADD CONSTRAINT student_evaluation_pkey PRIMARY KEY (id);


--
-- TOC entry 2350 (class 2606 OID 11340768)
-- Name: student_test_schedule_log_pkey; Type: CONSTRAINT; Schema: public; Owner: siette; Tablespace: 
--

ALTER TABLE ONLY student_test_schedule_log
    ADD CONSTRAINT student_test_schedule_log_pkey PRIMARY KEY (id);


--
-- TOC entry 2352 (class 2606 OID 11340773)
-- Name: student_type_pkey; Type: CONSTRAINT; Schema: public; Owner: siette; Tablespace: 
--

ALTER TABLE ONLY student_type
    ADD CONSTRAINT student_type_pkey PRIMARY KEY (id);


--
-- TOC entry 2354 (class 2606 OID 11340775)
-- Name: student_type_value_key; Type: CONSTRAINT; Schema: public; Owner: siette; Tablespace: 
--

ALTER TABLE ONLY student_type
    ADD CONSTRAINT student_type_value_key UNIQUE (value);


--
-- TOC entry 2356 (class 2606 OID 11340783)
-- Name: subject_pkey; Type: CONSTRAINT; Schema: public; Owner: siette; Tablespace: 
--

ALTER TABLE ONLY subject
    ADD CONSTRAINT subject_pkey PRIMARY KEY (id);


--
-- TOC entry 2358 (class 2606 OID 11340793)
-- Name: test_externalid_key; Type: CONSTRAINT; Schema: public; Owner: siette; Tablespace: 
--

ALTER TABLE ONLY test
    ADD CONSTRAINT test_externalid_key UNIQUE (externalid);


--
-- TOC entry 2360 (class 2606 OID 11340791)
-- Name: test_pkey; Type: CONSTRAINT; Schema: public; Owner: siette; Tablespace: 
--

ALTER TABLE ONLY test
    ADD CONSTRAINT test_pkey PRIMARY KEY (id);


--
-- TOC entry 2362 (class 2606 OID 11340801)
-- Name: test_sync_pkey; Type: CONSTRAINT; Schema: public; Owner: siette; Tablespace: 
--

ALTER TABLE ONLY test_sync
    ADD CONSTRAINT test_sync_pkey PRIMARY KEY (id);


--
-- TOC entry 2364 (class 2606 OID 11340809)
-- Name: test_type_pkey; Type: CONSTRAINT; Schema: public; Owner: siette; Tablespace: 
--

ALTER TABLE ONLY test_type
    ADD CONSTRAINT test_type_pkey PRIMARY KEY (id);


--
-- TOC entry 2366 (class 2606 OID 11340814)
-- Name: time_block_pkey; Type: CONSTRAINT; Schema: public; Owner: siette; Tablespace: 
--

ALTER TABLE ONLY time_block
    ADD CONSTRAINT time_block_pkey PRIMARY KEY (id);


--
-- TOC entry 2402 (class 2606 OID 11340904)
-- Name: time_block_student_type_pkey; Type: CONSTRAINT; Schema: public; Owner: siette; Tablespace: 
--

ALTER TABLE ONLY time_block_student_type
    ADD CONSTRAINT time_block_student_type_pkey PRIMARY KEY (time_block_id, student_type_id);


--
-- TOC entry 2368 (class 2606 OID 11340822)
-- Name: transport_pkey; Type: CONSTRAINT; Schema: public; Owner: siette; Tablespace: 
--

ALTER TABLE ONLY transport
    ADD CONSTRAINT transport_pkey PRIMARY KEY (id);


--
-- TOC entry 2404 (class 2606 OID 11340909)
-- Name: user_permission_pkey; Type: CONSTRAINT; Schema: public; Owner: siette; Tablespace: 
--

ALTER TABLE ONLY user_permission
    ADD CONSTRAINT user_permission_pkey PRIMARY KEY (user_id, permission_id);


--
-- TOC entry 2370 (class 2606 OID 11340832)
-- Name: user_profile_code_key; Type: CONSTRAINT; Schema: public; Owner: siette; Tablespace: 
--

ALTER TABLE ONLY user_profile
    ADD CONSTRAINT user_profile_code_key UNIQUE (code);


--
-- TOC entry 2372 (class 2606 OID 11340834)
-- Name: user_profile_name_key; Type: CONSTRAINT; Schema: public; Owner: siette; Tablespace: 
--

ALTER TABLE ONLY user_profile
    ADD CONSTRAINT user_profile_name_key UNIQUE (name);


--
-- TOC entry 2406 (class 2606 OID 11340914)
-- Name: user_profile_permission_pkey; Type: CONSTRAINT; Schema: public; Owner: siette; Tablespace: 
--

ALTER TABLE ONLY user_profile_permission
    ADD CONSTRAINT user_profile_permission_pkey PRIMARY KEY (user_profile_id, permission_id);


--
-- TOC entry 2374 (class 2606 OID 11340830)
-- Name: user_profile_pkey; Type: CONSTRAINT; Schema: public; Owner: siette; Tablespace: 
--

ALTER TABLE ONLY user_profile
    ADD CONSTRAINT user_profile_pkey PRIMARY KEY (id);


--
-- TOC entry 2376 (class 2606 OID 11340839)
-- Name: user_role_pkey; Type: CONSTRAINT; Schema: public; Owner: siette; Tablespace: 
--

ALTER TABLE ONLY user_role
    ADD CONSTRAINT user_role_pkey PRIMARY KEY (user_id, role_id);


--
-- TOC entry 2378 (class 2606 OID 11340844)
-- Name: user_user_profile_pkey; Type: CONSTRAINT; Schema: public; Owner: siette; Tablespace: 
--

ALTER TABLE ONLY user_user_profile
    ADD CONSTRAINT user_user_profile_pkey PRIMARY KEY (user_id, user_profile_id);


--
-- TOC entry 2380 (class 2606 OID 11340854)
-- Name: week_day_code_key; Type: CONSTRAINT; Schema: public; Owner: siette; Tablespace: 
--

ALTER TABLE ONLY week_day
    ADD CONSTRAINT week_day_code_key UNIQUE (code);


--
-- TOC entry 2382 (class 2606 OID 11340852)
-- Name: week_day_pkey; Type: CONSTRAINT; Schema: public; Owner: siette; Tablespace: 
--

ALTER TABLE ONLY week_day
    ADD CONSTRAINT week_day_pkey PRIMARY KEY (id);


--
-- TOC entry 2409 (class 2606 OID 11340925)
-- Name: fk103b22e18db7d; Type: FK CONSTRAINT; Schema: public; Owner: siette
--

ALTER TABLE ONLY cap
    ADD CONSTRAINT fk103b22e18db7d FOREIGN KEY (server_id) REFERENCES server(id);


--
-- TOC entry 2410 (class 2606 OID 11340930)
-- Name: fk103b278ccad1d; Type: FK CONSTRAINT; Schema: public; Owner: siette
--

ALTER TABLE ONLY cap
    ADD CONSTRAINT fk103b278ccad1d FOREIGN KEY (evaluationcenter_id) REFERENCES evaluation_center(id);


--
-- TOC entry 2460 (class 2606 OID 11341180)
-- Name: fk12d7d78ccad1d; Type: FK CONSTRAINT; Schema: public; Owner: siette
--

ALTER TABLE ONLY net
    ADD CONSTRAINT fk12d7d78ccad1d FOREIGN KEY (evaluationcenter_id) REFERENCES evaluation_center(id);


--
-- TOC entry 2436 (class 2606 OID 11341060)
-- Name: fk1d516c3016a821fe; Type: FK CONSTRAINT; Schema: public; Owner: siette
--

ALTER TABLE ONLY evaluation_event_evaluation_center
    ADD CONSTRAINT fk1d516c3016a821fe FOREIGN KEY (evaluation_event_id) REFERENCES evaluation_event(id);


--
-- TOC entry 2437 (class 2606 OID 11341065)
-- Name: fk1d516c3091147796; Type: FK CONSTRAINT; Schema: public; Owner: siette
--

ALTER TABLE ONLY evaluation_event_evaluation_center
    ADD CONSTRAINT fk1d516c3091147796 FOREIGN KEY (evaluation_center_id) REFERENCES evaluation_center(id);


--
-- TOC entry 2416 (class 2606 OID 11340960)
-- Name: fk1e1536075e331307; Type: FK CONSTRAINT; Schema: public; Owner: siette
--

ALTER TABLE ONLY classroom_time_block
    ADD CONSTRAINT fk1e1536075e331307 FOREIGN KEY (evaluation_event_classroom_id) REFERENCES evaluation_event_classroom(id);


--
-- TOC entry 2415 (class 2606 OID 11340955)
-- Name: fk1e153607c4af797c; Type: FK CONSTRAINT; Schema: public; Owner: siette
--

ALTER TABLE ONLY classroom_time_block
    ADD CONSTRAINT fk1e153607c4af797c FOREIGN KEY (time_block_id) REFERENCES time_block(id);


--
-- TOC entry 2417 (class 2606 OID 11340965)
-- Name: fk1e153607e33f712a; Type: FK CONSTRAINT; Schema: public; Owner: siette
--

ALTER TABLE ONLY classroom_time_block
    ADD CONSTRAINT fk1e153607e33f712a FOREIGN KEY (available_state_id) REFERENCES available_state(id);


--
-- TOC entry 2459 (class 2606 OID 11341175)
-- Name: fk1e3d582c64eda1b7; Type: FK CONSTRAINT; Schema: public; Owner: siette
--

ALTER TABLE ONLY matter_test_student
    ADD CONSTRAINT fk1e3d582c64eda1b7 FOREIGN KEY (session_id) REFERENCES session(id);


--
-- TOC entry 2456 (class 2606 OID 11341160)
-- Name: fk1e3d582c7deaab7; Type: FK CONSTRAINT; Schema: public; Owner: siette
--

ALTER TABLE ONLY matter_test_student
    ADD CONSTRAINT fk1e3d582c7deaab7 FOREIGN KEY (evaluationeventmattertest_id) REFERENCES evaluation_event_matter_test(id);


--
-- TOC entry 2458 (class 2606 OID 11341170)
-- Name: fk1e3d582c9d7d15f7; Type: FK CONSTRAINT; Schema: public; Owner: siette
--

ALTER TABLE ONLY matter_test_student
    ADD CONSTRAINT fk1e3d582c9d7d15f7 FOREIGN KEY (studentevaluation_id) REFERENCES student_evaluation(id);


--
-- TOC entry 2457 (class 2606 OID 11341165)
-- Name: fk1e3d582cea3772dd; Type: FK CONSTRAINT; Schema: public; Owner: siette
--

ALTER TABLE ONLY matter_test_student
    ADD CONSTRAINT fk1e3d582cea3772dd FOREIGN KEY (evaluationassignmentmatter_id) REFERENCES evaluation_assignment_matter(id);


--
-- TOC entry 2408 (class 2606 OID 11340920)
-- Name: fk1efe3c1ee6239d; Type: FK CONSTRAINT; Schema: public; Owner: siette
--

ALTER TABLE ONLY bank
    ADD CONSTRAINT fk1efe3c1ee6239d FOREIGN KEY (department_id) REFERENCES department(id);


--
-- TOC entry 2476 (class 2606 OID 11341260)
-- Name: fk25cc26e53709ff23; Type: FK CONSTRAINT; Schema: public; Owner: siette
--

ALTER TABLE ONLY student_test_schedule_log
    ADD CONSTRAINT fk25cc26e53709ff23 FOREIGN KEY (classroom_time_block_id) REFERENCES classroom_time_block(id);


--
-- TOC entry 2474 (class 2606 OID 11341250)
-- Name: fk25cc26e5d3df6696; Type: FK CONSTRAINT; Schema: public; Owner: siette
--

ALTER TABLE ONLY student_test_schedule_log
    ADD CONSTRAINT fk25cc26e5d3df6696 FOREIGN KEY (evaluation_assignment_id) REFERENCES evaluation_assignment(id);


--
-- TOC entry 2475 (class 2606 OID 11341255)
-- Name: fk25cc26e5f7571133; Type: FK CONSTRAINT; Schema: public; Owner: siette
--

ALTER TABLE ONLY student_test_schedule_log
    ADD CONSTRAINT fk25cc26e5f7571133 FOREIGN KEY (matter_test_student_id) REFERENCES matter_test_student(id);


--
-- TOC entry 2477 (class 2606 OID 11341265)
-- Name: fk273c929af5199d; Type: FK CONSTRAINT; Schema: public; Owner: siette
--

ALTER TABLE ONLY test
    ADD CONSTRAINT fk273c929af5199d FOREIGN KEY (bank_id) REFERENCES bank(id);


--
-- TOC entry 2479 (class 2606 OID 11341275)
-- Name: fk273c92d59b8bbd; Type: FK CONSTRAINT; Schema: public; Owner: siette
--

ALTER TABLE ONLY test
    ADD CONSTRAINT fk273c92d59b8bbd FOREIGN KEY (evaluationtype_id) REFERENCES evaluation_type(id);


--
-- TOC entry 2478 (class 2606 OID 11341270)
-- Name: fk273c92f4861a3d; Type: FK CONSTRAINT; Schema: public; Owner: siette
--

ALTER TABLE ONLY test
    ADD CONSTRAINT fk273c92f4861a3d FOREIGN KEY (testtype_id) REFERENCES test_type(id);


--
-- TOC entry 2506 (class 2606 OID 11341410)
-- Name: fk30ba72c33ed7b67d; Type: FK CONSTRAINT; Schema: public; Owner: siette
--

ALTER TABLE ONLY user_permission
    ADD CONSTRAINT fk30ba72c33ed7b67d FOREIGN KEY (permission_id) REFERENCES permission(id);


--
-- TOC entry 2507 (class 2606 OID 11341415)
-- Name: fk30ba72c3a64ffb3d; Type: FK CONSTRAINT; Schema: public; Owner: siette
--

ALTER TABLE ONLY user_permission
    ADD CONSTRAINT fk30ba72c3a64ffb3d FOREIGN KEY (user_id) REFERENCES evs_user(id);


--
-- TOC entry 2487 (class 2606 OID 11341315)
-- Name: fk35642df6326ca78e; Type: FK CONSTRAINT; Schema: public; Owner: siette
--

ALTER TABLE ONLY classroom_time_block_student_type
    ADD CONSTRAINT fk35642df6326ca78e FOREIGN KEY (student_type_id) REFERENCES student_type(id);


--
-- TOC entry 2486 (class 2606 OID 11341310)
-- Name: fk35642df63709ff23; Type: FK CONSTRAINT; Schema: public; Owner: siette
--

ALTER TABLE ONLY classroom_time_block_student_type
    ADD CONSTRAINT fk35642df63709ff23 FOREIGN KEY (classroom_time_block_id) REFERENCES classroom_time_block(id);


--
-- TOC entry 2461 (class 2606 OID 11341185)
-- Name: fk39a2d0b9a64ffb3d; Type: FK CONSTRAINT; Schema: public; Owner: siette
--

ALTER TABLE ONLY registration
    ADD CONSTRAINT fk39a2d0b9a64ffb3d FOREIGN KEY (user_id) REFERENCES evs_user(id);


--
-- TOC entry 2462 (class 2606 OID 11341190)
-- Name: fk39a2d0b9f77fea3d; Type: FK CONSTRAINT; Schema: public; Owner: siette
--

ALTER TABLE ONLY registration
    ADD CONSTRAINT fk39a2d0b9f77fea3d FOREIGN KEY (center_id) REFERENCES center(id);


--
-- TOC entry 2446 (class 2606 OID 11341110)
-- Name: fk413feac85a4b71dd; Type: FK CONSTRAINT; Schema: public; Owner: siette
--

ALTER TABLE ONLY evaluation_event_matter_test
    ADD CONSTRAINT fk413feac85a4b71dd FOREIGN KEY (test_id) REFERENCES test(id);


--
-- TOC entry 2445 (class 2606 OID 11341105)
-- Name: fk413feac8a4faee57; Type: FK CONSTRAINT; Schema: public; Owner: siette
--

ALTER TABLE ONLY evaluation_event_matter_test
    ADD CONSTRAINT fk413feac8a4faee57 FOREIGN KEY (evaluationeventmatter_id) REFERENCES evaluation_event_matter(id);


--
-- TOC entry 2488 (class 2606 OID 11341320)
-- Name: fk41c668e716a821fe; Type: FK CONSTRAINT; Schema: public; Owner: siette
--

ALTER TABLE ONLY evaluation_event_admin
    ADD CONSTRAINT fk41c668e716a821fe FOREIGN KEY (evaluation_event_id) REFERENCES evaluation_event(id);


--
-- TOC entry 2489 (class 2606 OID 11341325)
-- Name: fk41c668e7a64ffb3d; Type: FK CONSTRAINT; Schema: public; Owner: siette
--

ALTER TABLE ONLY evaluation_event_admin
    ADD CONSTRAINT fk41c668e7a64ffb3d FOREIGN KEY (user_id) REFERENCES evs_user(id);


--
-- TOC entry 2430 (class 2606 OID 11341030)
-- Name: fk42b533ab1172d6d7; Type: FK CONSTRAINT; Schema: public; Owner: siette
--

ALTER TABLE ONLY evaluation_event_classroom
    ADD CONSTRAINT fk42b533ab1172d6d7 FOREIGN KEY (net_id) REFERENCES net(id);


--
-- TOC entry 2433 (class 2606 OID 11341045)
-- Name: fk42b533ab6e01cbb6; Type: FK CONSTRAINT; Schema: public; Owner: siette
--

ALTER TABLE ONLY evaluation_event_classroom
    ADD CONSTRAINT fk42b533ab6e01cbb6 FOREIGN KEY (evaluation_event_evaluation_center_id) REFERENCES evaluation_event_evaluation_center(id);


--
-- TOC entry 2431 (class 2606 OID 11341035)
-- Name: fk42b533abc73d8e3d; Type: FK CONSTRAINT; Schema: public; Owner: siette
--

ALTER TABLE ONLY evaluation_event_classroom
    ADD CONSTRAINT fk42b533abc73d8e3d FOREIGN KEY (schedule_id) REFERENCES schedule(id);


--
-- TOC entry 2432 (class 2606 OID 11341040)
-- Name: fk42b533abd55eb157; Type: FK CONSTRAINT; Schema: public; Owner: siette
--

ALTER TABLE ONLY evaluation_event_classroom
    ADD CONSTRAINT fk42b533abd55eb157 FOREIGN KEY (classroom_id) REFERENCES classroom(id);


--
-- TOC entry 2434 (class 2606 OID 11341050)
-- Name: fk42b533abfe732eb7; Type: FK CONSTRAINT; Schema: public; Owner: siette
--

ALTER TABLE ONLY evaluation_event_classroom
    ADD CONSTRAINT fk42b533abfe732eb7 FOREIGN KEY (cap_id) REFERENCES cap(id);


--
-- TOC entry 2424 (class 2606 OID 11341000)
-- Name: fk495ab7b015df9f97; Type: FK CONSTRAINT; Schema: public; Owner: siette
--

ALTER TABLE ONLY evaluation_assignment
    ADD CONSTRAINT fk495ab7b015df9f97 FOREIGN KEY (evaluationevent_id) REFERENCES evaluation_event(id);


--
-- TOC entry 2426 (class 2606 OID 11341010)
-- Name: fk495ab7b067ab7ef7; Type: FK CONSTRAINT; Schema: public; Owner: siette
--

ALTER TABLE ONLY evaluation_assignment
    ADD CONSTRAINT fk495ab7b067ab7ef7 FOREIGN KEY (studenttype_id) REFERENCES student_type(id);


--
-- TOC entry 2425 (class 2606 OID 11341005)
-- Name: fk495ab7b0a64ffb3d; Type: FK CONSTRAINT; Schema: public; Owner: siette
--

ALTER TABLE ONLY evaluation_assignment
    ADD CONSTRAINT fk495ab7b0a64ffb3d FOREIGN KEY (user_id) REFERENCES evs_user(id);


--
-- TOC entry 2464 (class 2606 OID 11341200)
-- Name: fk4c98ce44d3df6696; Type: FK CONSTRAINT; Schema: public; Owner: siette
--

ALTER TABLE ONLY schedule_modification
    ADD CONSTRAINT fk4c98ce44d3df6696 FOREIGN KEY (evaluation_assignment_id) REFERENCES evaluation_assignment(id);


--
-- TOC entry 2463 (class 2606 OID 11341195)
-- Name: fk50c829716a821fe; Type: FK CONSTRAINT; Schema: public; Owner: siette
--

ALTER TABLE ONLY schedule
    ADD CONSTRAINT fk50c829716a821fe FOREIGN KEY (evaluation_event_id) REFERENCES evaluation_event(id);


--
-- TOC entry 2420 (class 2606 OID 11340980)
-- Name: fk52a72cdd43cdb41b; Type: FK CONSTRAINT; Schema: public; Owner: siette
--

ALTER TABLE ONLY component
    ADD CONSTRAINT fk52a72cdd43cdb41b FOREIGN KEY (manager_id) REFERENCES evs_user(id);


--
-- TOC entry 2419 (class 2606 OID 11340975)
-- Name: fk52a72cdd65d8927d; Type: FK CONSTRAINT; Schema: public; Owner: siette
--

ALTER TABLE ONLY component
    ADD CONSTRAINT fk52a72cdd65d8927d FOREIGN KEY (academicperiod_id) REFERENCES academic_period(id);


--
-- TOC entry 2418 (class 2606 OID 11340970)
-- Name: fk52a72cddd5807677; Type: FK CONSTRAINT; Schema: public; Owner: siette
--

ALTER TABLE ONLY component
    ADD CONSTRAINT fk52a72cddd5807677 FOREIGN KEY (academiclevel_id) REFERENCES academic_level(id);


--
-- TOC entry 2421 (class 2606 OID 11340985)
-- Name: fk52a72cddf8330c3d; Type: FK CONSTRAINT; Schema: public; Owner: siette
--

ALTER TABLE ONLY component
    ADD CONSTRAINT fk52a72cddf8330c3d FOREIGN KEY (mode_id) REFERENCES mode(id);


--
-- TOC entry 2452 (class 2606 OID 11341140)
-- Name: fk5f1bb4fa70e7c2bd; Type: FK CONSTRAINT; Schema: public; Owner: siette
--

ALTER TABLE ONLY matter_bank
    ADD CONSTRAINT fk5f1bb4fa70e7c2bd FOREIGN KEY (matter_id) REFERENCES matter(id);


--
-- TOC entry 2453 (class 2606 OID 11341145)
-- Name: fk5f1bb4fa9af5199d; Type: FK CONSTRAINT; Schema: public; Owner: siette
--

ALTER TABLE ONLY matter_bank
    ADD CONSTRAINT fk5f1bb4fa9af5199d FOREIGN KEY (bank_id) REFERENCES bank(id);


--
-- TOC entry 2480 (class 2606 OID 11341280)
-- Name: fk61839a2815df9f97; Type: FK CONSTRAINT; Schema: public; Owner: siette
--

ALTER TABLE ONLY test_sync
    ADD CONSTRAINT fk61839a2815df9f97 FOREIGN KEY (evaluationevent_id) REFERENCES evaluation_event(id);


--
-- TOC entry 2438 (class 2606 OID 11341070)
-- Name: fk66b3b446e01cbb6; Type: FK CONSTRAINT; Schema: public; Owner: siette
--

ALTER TABLE ONLY evaluation_event_evaluation_center_center
    ADD CONSTRAINT fk66b3b446e01cbb6 FOREIGN KEY (evaluation_event_evaluation_center_id) REFERENCES evaluation_event_evaluation_center(id);


--
-- TOC entry 2439 (class 2606 OID 11341075)
-- Name: fk66b3b44f77fea3d; Type: FK CONSTRAINT; Schema: public; Owner: siette
--

ALTER TABLE ONLY evaluation_event_evaluation_center_center
    ADD CONSTRAINT fk66b3b44f77fea3d FOREIGN KEY (center_id) REFERENCES center(id);


--
-- TOC entry 2411 (class 2606 OID 11340935)
-- Name: fk7645c05578ccad1d; Type: FK CONSTRAINT; Schema: public; Owner: siette
--

ALTER TABLE ONLY center
    ADD CONSTRAINT fk7645c05578ccad1d FOREIGN KEY (evaluationcenter_id) REFERENCES evaluation_center(id);


--
-- TOC entry 2454 (class 2606 OID 11341150)
-- Name: fk7e0bc98e70e7c2bd; Type: FK CONSTRAINT; Schema: public; Owner: siette
--

ALTER TABLE ONLY matter_subject
    ADD CONSTRAINT fk7e0bc98e70e7c2bd FOREIGN KEY (matter_id) REFERENCES matter(id);


--
-- TOC entry 2455 (class 2606 OID 11341155)
-- Name: fk7e0bc98efa0891f7; Type: FK CONSTRAINT; Schema: public; Owner: siette
--

ALTER TABLE ONLY matter_subject
    ADD CONSTRAINT fk7e0bc98efa0891f7 FOREIGN KEY (subject_id) REFERENCES subject(id);


--
-- TOC entry 2451 (class 2606 OID 11341135)
-- Name: fk872092c1a64ffb3d; Type: FK CONSTRAINT; Schema: public; Owner: siette
--

ALTER TABLE ONLY matter
    ADD CONSTRAINT fk872092c1a64ffb3d FOREIGN KEY (user_id) REFERENCES evs_user(id);


--
-- TOC entry 2450 (class 2606 OID 11341130)
-- Name: fk872092c1d5807677; Type: FK CONSTRAINT; Schema: public; Owner: siette
--

ALTER TABLE ONLY matter
    ADD CONSTRAINT fk872092c1d5807677 FOREIGN KEY (academiclevel_id) REFERENCES academic_level(id);


--
-- TOC entry 2496 (class 2606 OID 11341360)
-- Name: fk8dceb1ac2b809be7; Type: FK CONSTRAINT; Schema: public; Owner: siette
--

ALTER TABLE ONLY evaluation_event_teacher_classroom_time_block
    ADD CONSTRAINT fk8dceb1ac2b809be7 FOREIGN KEY (evaluation_event_teacher_id) REFERENCES evaluation_event_teacher(id);


--
-- TOC entry 2497 (class 2606 OID 11341365)
-- Name: fk8dceb1ac3709ff23; Type: FK CONSTRAINT; Schema: public; Owner: siette
--

ALTER TABLE ONLY evaluation_event_teacher_classroom_time_block
    ADD CONSTRAINT fk8dceb1ac3709ff23 FOREIGN KEY (classroom_time_block_id) REFERENCES classroom_time_block(id);


--
-- TOC entry 2441 (class 2606 OID 11341085)
-- Name: fk8dd5d04915df9f97; Type: FK CONSTRAINT; Schema: public; Owner: siette
--

ALTER TABLE ONLY evaluation_event_matter
    ADD CONSTRAINT fk8dd5d04915df9f97 FOREIGN KEY (evaluationevent_id) REFERENCES evaluation_event(id);


--
-- TOC entry 2443 (class 2606 OID 11341095)
-- Name: fk8dd5d04965d8927d; Type: FK CONSTRAINT; Schema: public; Owner: siette
--

ALTER TABLE ONLY evaluation_event_matter
    ADD CONSTRAINT fk8dd5d04965d8927d FOREIGN KEY (academicperiod_id) REFERENCES academic_period(id);


--
-- TOC entry 2440 (class 2606 OID 11341080)
-- Name: fk8dd5d04970e7c2bd; Type: FK CONSTRAINT; Schema: public; Owner: siette
--

ALTER TABLE ONLY evaluation_event_matter
    ADD CONSTRAINT fk8dd5d04970e7c2bd FOREIGN KEY (matter_id) REFERENCES matter(id);


--
-- TOC entry 2442 (class 2606 OID 11341090)
-- Name: fk8dd5d0499af5199d; Type: FK CONSTRAINT; Schema: public; Owner: siette
--

ALTER TABLE ONLY evaluation_event_matter
    ADD CONSTRAINT fk8dd5d0499af5199d FOREIGN KEY (bank_id) REFERENCES bank(id);


--
-- TOC entry 2444 (class 2606 OID 11341100)
-- Name: fk8dd5d049f8330c3d; Type: FK CONSTRAINT; Schema: public; Owner: siette
--

ALTER TABLE ONLY evaluation_event_matter
    ADD CONSTRAINT fk8dd5d049f8330c3d FOREIGN KEY (mode_id) REFERENCES mode(id);


--
-- TOC entry 2481 (class 2606 OID 11341285)
-- Name: fk96476c7bc73d8e3d; Type: FK CONSTRAINT; Schema: public; Owner: siette
--

ALTER TABLE ONLY time_block
    ADD CONSTRAINT fk96476c7bc73d8e3d FOREIGN KEY (schedule_id) REFERENCES schedule(id);


--
-- TOC entry 2470 (class 2606 OID 11341230)
-- Name: fk9ab1ebe01172d6d7; Type: FK CONSTRAINT; Schema: public; Owner: siette
--

ALTER TABLE ONLY student_evaluation
    ADD CONSTRAINT fk9ab1ebe01172d6d7 FOREIGN KEY (net_id) REFERENCES net(id);


--
-- TOC entry 2471 (class 2606 OID 11341235)
-- Name: fk9ab1ebe02e18db7d; Type: FK CONSTRAINT; Schema: public; Owner: siette
--

ALTER TABLE ONLY student_evaluation
    ADD CONSTRAINT fk9ab1ebe02e18db7d FOREIGN KEY (server_id) REFERENCES server(id);


--
-- TOC entry 2473 (class 2606 OID 11341245)
-- Name: fk9ab1ebe03709ff23; Type: FK CONSTRAINT; Schema: public; Owner: siette
--

ALTER TABLE ONLY student_evaluation
    ADD CONSTRAINT fk9ab1ebe03709ff23 FOREIGN KEY (classroom_time_block_id) REFERENCES classroom_time_block(id);


--
-- TOC entry 2472 (class 2606 OID 11341240)
-- Name: fk9ab1ebe0d3df6696; Type: FK CONSTRAINT; Schema: public; Owner: siette
--

ALTER TABLE ONLY student_evaluation
    ADD CONSTRAINT fk9ab1ebe0d3df6696 FOREIGN KEY (evaluation_assignment_id) REFERENCES evaluation_assignment(id);


--
-- TOC entry 2505 (class 2606 OID 11341405)
-- Name: fk9c5783e2326ca78e; Type: FK CONSTRAINT; Schema: public; Owner: siette
--

ALTER TABLE ONLY time_block_student_type
    ADD CONSTRAINT fk9c5783e2326ca78e FOREIGN KEY (student_type_id) REFERENCES student_type(id);


--
-- TOC entry 2504 (class 2606 OID 11341400)
-- Name: fk9c5783e2c4af797c; Type: FK CONSTRAINT; Schema: public; Owner: siette
--

ALTER TABLE ONLY time_block_student_type
    ADD CONSTRAINT fk9c5783e2c4af797c FOREIGN KEY (time_block_id) REFERENCES time_block(id);


--
-- TOC entry 2449 (class 2606 OID 11341125)
-- Name: fk9e77cb0416a821fe; Type: FK CONSTRAINT; Schema: public; Owner: siette
--

ALTER TABLE ONLY generic_schedule_log
    ADD CONSTRAINT fk9e77cb0416a821fe FOREIGN KEY (evaluation_event_id) REFERENCES evaluation_event(id);


--
-- TOC entry 2448 (class 2606 OID 11341120)
-- Name: fka4efcb3a16a821fe; Type: FK CONSTRAINT; Schema: public; Owner: siette
--

ALTER TABLE ONLY evaluation_event_teacher
    ADD CONSTRAINT fka4efcb3a16a821fe FOREIGN KEY (evaluation_event_id) REFERENCES evaluation_event(id);


--
-- TOC entry 2447 (class 2606 OID 11341115)
-- Name: fka4efcb3aa64ffb3d; Type: FK CONSTRAINT; Schema: public; Owner: siette
--

ALTER TABLE ONLY evaluation_event_teacher
    ADD CONSTRAINT fka4efcb3aa64ffb3d FOREIGN KEY (user_id) REFERENCES evs_user(id);


--
-- TOC entry 2412 (class 2606 OID 11340940)
-- Name: fka591be331172d6d7; Type: FK CONSTRAINT; Schema: public; Owner: siette
--

ALTER TABLE ONLY classroom
    ADD CONSTRAINT fka591be331172d6d7 FOREIGN KEY (net_id) REFERENCES net(id);


--
-- TOC entry 2413 (class 2606 OID 11340945)
-- Name: fka591be3378ccad1d; Type: FK CONSTRAINT; Schema: public; Owner: siette
--

ALTER TABLE ONLY classroom
    ADD CONSTRAINT fka591be3378ccad1d FOREIGN KEY (evaluationcenter_id) REFERENCES evaluation_center(id);


--
-- TOC entry 2414 (class 2606 OID 11340950)
-- Name: fka591be33fe732eb7; Type: FK CONSTRAINT; Schema: public; Owner: siette
--

ALTER TABLE ONLY classroom
    ADD CONSTRAINT fka591be33fe732eb7 FOREIGN KEY (cap_id) REFERENCES cap(id);


--
-- TOC entry 2492 (class 2606 OID 11341340)
-- Name: fkb6f26286a4faee57; Type: FK CONSTRAINT; Schema: public; Owner: siette
--

ALTER TABLE ONLY evaluation_event_matter_day
    ADD CONSTRAINT fkb6f26286a4faee57 FOREIGN KEY (evaluationeventmatter_id) REFERENCES evaluation_event_matter(id);


--
-- TOC entry 2493 (class 2606 OID 11341345)
-- Name: fkb6f26286a4ff156e; Type: FK CONSTRAINT; Schema: public; Owner: siette
--

ALTER TABLE ONLY evaluation_event_matter_day
    ADD CONSTRAINT fkb6f26286a4ff156e FOREIGN KEY (daysallowed_id) REFERENCES week_day(id);


--
-- TOC entry 2495 (class 2606 OID 11341355)
-- Name: fkb86c3647a415a076; Type: FK CONSTRAINT; Schema: public; Owner: siette
--

ALTER TABLE ONLY evaluation_event_matter_evaluation_type
    ADD CONSTRAINT fkb86c3647a415a076 FOREIGN KEY (evaluation_type_id) REFERENCES evaluation_type(id);


--
-- TOC entry 2494 (class 2606 OID 11341350)
-- Name: fkb86c3647a4faee57; Type: FK CONSTRAINT; Schema: public; Owner: siette
--

ALTER TABLE ONLY evaluation_event_matter_evaluation_type
    ADD CONSTRAINT fkb86c3647a4faee57 FOREIGN KEY (evaluationeventmatter_id) REFERENCES evaluation_event_matter(id);


--
-- TOC entry 2482 (class 2606 OID 11341290)
-- Name: fkbc16f46a125375d; Type: FK CONSTRAINT; Schema: public; Owner: siette
--

ALTER TABLE ONLY user_role
    ADD CONSTRAINT fkbc16f46a125375d FOREIGN KEY (role_id) REFERENCES role(id);


--
-- TOC entry 2483 (class 2606 OID 11341295)
-- Name: fkbc16f46aa64ffb3d; Type: FK CONSTRAINT; Schema: public; Owner: siette
--

ALTER TABLE ONLY user_role
    ADD CONSTRAINT fkbc16f46aa64ffb3d FOREIGN KEY (user_id) REFERENCES evs_user(id);


--
-- TOC entry 2509 (class 2606 OID 11341425)
-- Name: fkbcde97193ed7b67d; Type: FK CONSTRAINT; Schema: public; Owner: siette
--

ALTER TABLE ONLY user_profile_permission
    ADD CONSTRAINT fkbcde97193ed7b67d FOREIGN KEY (permission_id) REFERENCES permission(id);


--
-- TOC entry 2508 (class 2606 OID 11341420)
-- Name: fkbcde971997ab4d00; Type: FK CONSTRAINT; Schema: public; Owner: siette
--

ALTER TABLE ONLY user_profile_permission
    ADD CONSTRAINT fkbcde971997ab4d00 FOREIGN KEY (user_profile_id) REFERENCES user_profile(id);


--
-- TOC entry 2503 (class 2606 OID 11341395)
-- Name: fkc3ca1ba6b090997d; Type: FK CONSTRAINT; Schema: public; Owner: siette
--

ALTER TABLE ONLY registration_subject
    ADD CONSTRAINT fkc3ca1ba6b090997d FOREIGN KEY (registration_id) REFERENCES registration(id);


--
-- TOC entry 2502 (class 2606 OID 11341390)
-- Name: fkc3ca1ba6fa0891f7; Type: FK CONSTRAINT; Schema: public; Owner: siette
--

ALTER TABLE ONLY registration_subject
    ADD CONSTRAINT fkc3ca1ba6fa0891f7 FOREIGN KEY (subject_id) REFERENCES subject(id);


--
-- TOC entry 2484 (class 2606 OID 11341300)
-- Name: fkcd41ed0997ab4d00; Type: FK CONSTRAINT; Schema: public; Owner: siette
--

ALTER TABLE ONLY user_user_profile
    ADD CONSTRAINT fkcd41ed0997ab4d00 FOREIGN KEY (user_profile_id) REFERENCES user_profile(id);


--
-- TOC entry 2485 (class 2606 OID 11341305)
-- Name: fkcd41ed09a64ffb3d; Type: FK CONSTRAINT; Schema: public; Owner: siette
--

ALTER TABLE ONLY user_user_profile
    ADD CONSTRAINT fkcd41ed09a64ffb3d FOREIGN KEY (user_id) REFERENCES evs_user(id);


--
-- TOC entry 2465 (class 2606 OID 11341205)
-- Name: fkd21c1cc916a821fe; Type: FK CONSTRAINT; Schema: public; Owner: siette
--

ALTER TABLE ONLY schedule_modification_date
    ADD CONSTRAINT fkd21c1cc916a821fe FOREIGN KEY (evaluation_event_id) REFERENCES evaluation_event(id);


--
-- TOC entry 2468 (class 2606 OID 11341220)
-- Name: fkd21e91c9516d0b; Type: FK CONSTRAINT; Schema: public; Owner: siette
--

ALTER TABLE ONLY schedule_modification_info
    ADD CONSTRAINT fkd21e91c9516d0b FOREIGN KEY (old_classroom_time_block_id) REFERENCES classroom_time_block(id);


--
-- TOC entry 2469 (class 2606 OID 11341225)
-- Name: fkd21e91c989a4cd84; Type: FK CONSTRAINT; Schema: public; Owner: siette
--

ALTER TABLE ONLY schedule_modification_info
    ADD CONSTRAINT fkd21e91c989a4cd84 FOREIGN KEY (new_classroom_time_block_id) REFERENCES classroom_time_block(id);


--
-- TOC entry 2466 (class 2606 OID 11341210)
-- Name: fkd21e91c9e45d4e2c; Type: FK CONSTRAINT; Schema: public; Owner: siette
--

ALTER TABLE ONLY schedule_modification_info
    ADD CONSTRAINT fkd21e91c9e45d4e2c FOREIGN KEY (schedule_modification_id) REFERENCES schedule_modification(id);


--
-- TOC entry 2467 (class 2606 OID 11341215)
-- Name: fkd21e91c9f7571133; Type: FK CONSTRAINT; Schema: public; Owner: siette
--

ALTER TABLE ONLY schedule_modification_info
    ADD CONSTRAINT fkd21e91c9f7571133 FOREIGN KEY (matter_test_student_id) REFERENCES matter_test_student(id);


--
-- TOC entry 2490 (class 2606 OID 11341330)
-- Name: fkd4a4adf516a821fe; Type: FK CONSTRAINT; Schema: public; Owner: siette
--

ALTER TABLE ONLY evaluation_event_evaluation_type
    ADD CONSTRAINT fkd4a4adf516a821fe FOREIGN KEY (evaluation_event_id) REFERENCES evaluation_event(id);


--
-- TOC entry 2491 (class 2606 OID 11341335)
-- Name: fkd4a4adf5a415a076; Type: FK CONSTRAINT; Schema: public; Owner: siette
--

ALTER TABLE ONLY evaluation_event_evaluation_type
    ADD CONSTRAINT fkd4a4adf5a415a076 FOREIGN KEY (evaluation_type_id) REFERENCES evaluation_type(id);


--
-- TOC entry 2422 (class 2606 OID 11340990)
-- Name: fke08d094515df9f97; Type: FK CONSTRAINT; Schema: public; Owner: siette
--

ALTER TABLE ONLY email_notifications
    ADD CONSTRAINT fke08d094515df9f97 FOREIGN KEY (evaluationevent_id) REFERENCES evaluation_event(id);


--
-- TOC entry 2423 (class 2606 OID 11340995)
-- Name: fke08d0945a64ffb3d; Type: FK CONSTRAINT; Schema: public; Owner: siette
--

ALTER TABLE ONLY email_notifications
    ADD CONSTRAINT fke08d0945a64ffb3d FOREIGN KEY (user_id) REFERENCES evs_user(id);


--
-- TOC entry 2407 (class 2606 OID 11340915)
-- Name: fke66327d478ccad1d; Type: FK CONSTRAINT; Schema: public; Owner: siette
--

ALTER TABLE ONLY address
    ADD CONSTRAINT fke66327d478ccad1d FOREIGN KEY (evaluationcenter_id) REFERENCES evaluation_center(id);


--
-- TOC entry 2428 (class 2606 OID 11341020)
-- Name: fke7d8d23011e3809d; Type: FK CONSTRAINT; Schema: public; Owner: siette
--

ALTER TABLE ONLY evaluation_assignment_matter
    ADD CONSTRAINT fke7d8d23011e3809d FOREIGN KEY (evaluationassignment_id) REFERENCES evaluation_assignment(id);


--
-- TOC entry 2427 (class 2606 OID 11341015)
-- Name: fke7d8d230a4faee57; Type: FK CONSTRAINT; Schema: public; Owner: siette
--

ALTER TABLE ONLY evaluation_assignment_matter
    ADD CONSTRAINT fke7d8d230a4faee57 FOREIGN KEY (evaluationeventmatter_id) REFERENCES evaluation_event_matter(id);


--
-- TOC entry 2429 (class 2606 OID 11341025)
-- Name: fke7d8d230f77fea3d; Type: FK CONSTRAINT; Schema: public; Owner: siette
--

ALTER TABLE ONLY evaluation_assignment_matter
    ADD CONSTRAINT fke7d8d230f77fea3d FOREIGN KEY (center_id) REFERENCES center(id);


--
-- TOC entry 2498 (class 2606 OID 11341370)
-- Name: fkee5b091e70e7c2bd; Type: FK CONSTRAINT; Schema: public; Owner: siette
--

ALTER TABLE ONLY matter_day
    ADD CONSTRAINT fkee5b091e70e7c2bd FOREIGN KEY (matter_id) REFERENCES matter(id);


--
-- TOC entry 2499 (class 2606 OID 11341375)
-- Name: fkee5b091ea4ff156e; Type: FK CONSTRAINT; Schema: public; Owner: siette
--

ALTER TABLE ONLY matter_day
    ADD CONSTRAINT fkee5b091ea4ff156e FOREIGN KEY (daysallowed_id) REFERENCES week_day(id);


--
-- TOC entry 2500 (class 2606 OID 11341380)
-- Name: fkf408e8252e18db7d; Type: FK CONSTRAINT; Schema: public; Owner: siette
--

ALTER TABLE ONLY net_server
    ADD CONSTRAINT fkf408e8252e18db7d FOREIGN KEY (server_id) REFERENCES server(id);


--
-- TOC entry 2501 (class 2606 OID 11341385)
-- Name: fkf408e825a19c7d77; Type: FK CONSTRAINT; Schema: public; Owner: siette
--

ALTER TABLE ONLY net_server
    ADD CONSTRAINT fkf408e825a19c7d77 FOREIGN KEY (net_id_id) REFERENCES net(id);


--
-- TOC entry 2435 (class 2606 OID 11341055)
-- Name: fkf7ed864e16a821fe; Type: FK CONSTRAINT; Schema: public; Owner: siette
--

ALTER TABLE ONLY evaluation_event_configuration
    ADD CONSTRAINT fkf7ed864e16a821fe FOREIGN KEY (evaluation_event_id) REFERENCES evaluation_event(id);


--
-- TOC entry 2584 (class 0 OID 0)
-- Dependencies: 5
-- Name: public; Type: ACL; Schema: -; Owner: siette
--

REVOKE ALL ON SCHEMA public FROM PUBLIC;
REVOKE ALL ON SCHEMA public FROM siette;
GRANT ALL ON SCHEMA public TO siette;
GRANT ALL ON SCHEMA public TO PUBLIC;


-- Completed on 2018-04-17 13:51:37

--
-- sietteQL database dump complete
--

