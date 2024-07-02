--퀴즈 테이블
--DROP TABLE quiz;

CREATE TABLE quiz(
	seq 			NUMBER 			PRIMARY KEY,	-- 퀴즈 고유번호
	quiz_content 	clob 			NOT NULL,		-- 퀴즈 문제내용
	quiz_result 	varchar2(100) 	NOT NULL,		-- 퀴즈 정답
	quiz_count 		NUMBER							-- 퀴즈 정답 수
);

-- comment 등록 sql
COMMENT ON COLUMN quiz.seq IS '퀴즈 고유번호';
COMMENT ON COLUMN quiz.quiz_content IS '퀴즈 문제내용';
COMMENT ON COLUMN quiz.quiz_result IS '퀴즈 정답';
COMMENT ON COLUMN quiz.quiz_count IS '퀴즈 정답 수';

--sequence생성
DROP SEQUENCE seq_quiz_no;
CREATE SEQUENCE seq_quiz_no
	INCREMENT BY 1
	START WITH 1;

SELECT * FROM QUIZ;