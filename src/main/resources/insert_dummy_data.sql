INSERT INTO member (member_id, name, phone, email, job_type, introduction, social_provider, provider_id, refresh_token,
                    created_at, updated_at)
VALUES (1, 'Savannah Cohen', '75835709111', 'john51@mitchell.com', 'DEV',
        'Religious talk there affect past have. Whether success subject and south.',
        'GOOGLE', '68ab9a1b-4a16-4bb7-b0a1-db5ba16dfc1b',
        'b697ced59045bfe68e1408d963d5de0a712de07c94084048ff2e678cc5be4720', NOW(), NOW());


INSERT INTO member (member_id, name, phone, email, job_type, introduction, social_provider, provider_id, refresh_token,
                    created_at, updated_at)
VALUES (2, 'Lori Nichols', '00478610732', 'stephaniewilson@jimenez.com', 'DEV',
        'Head join southern north. Behavior federal environmental sell alone behind culture.',
        'KAKAO', '84293f5b-c001-494c-a2f0-706714893d65',
        '7a9a97d396ab9d96b70d76a968033e64053a5dd2dbee8b7bf03ca89f4bb72081', NOW(), NOW());


INSERT INTO member (member_id, name, phone, email, job_type, introduction, social_provider, provider_id, refresh_token,
                    created_at, updated_at)
VALUES (3, 'Amy Garcia', '74341633104', 'hoganjoshua@hotmail.com', 'DEV',
        'Exist painting take just cut. Lawyer analysis close especially mention race.',
        'GOOGLE', 'b39ecb9c-e360-4a2c-9ac2-4a991699d93c',
        'b8dbf968073a026b2c50f5d73e9ab9bf3ce7ee1aaa98b6222ef974d47f8a577f', NOW(), NOW());


INSERT INTO member (member_id, name, phone, email, job_type, introduction, social_provider, provider_id, refresh_token,
                    created_at, updated_at)
VALUES (4, 'Latoya Morgan', '70662189439', 'xroberts@yahoo.com', 'DEV',
        'Loss decide way final quality soldier threat movement. Ability former thousand do.',
        'GOOGLE', 'cc7b5099-3c8e-432a-a1ba-e3f94fa7dc66',
        'a47dabb2f3e97ca9905ffb7087a099c71cfeefad6f657907e145f542b52e1b3a', NOW(), NOW());


INSERT INTO member (member_id, name, phone, email, job_type, introduction, social_provider, provider_id, refresh_token,
                    created_at, updated_at)
VALUES (5, 'Crystal Mclaughlin', '05497963156', 'danny95@williams-hobbs.com', 'DEV',
        'Receive figure any wish inside either among. Behavior radio recently. End crime network modern.',
        'GOOGLE', '14ba6ccc-601b-4a7b-92e1-075a317db784',
        'dd01202ff6b524a0560576401e9c0609c2f3c01669dd2aa3b927a00701964e6f', NOW(), NOW());

INSERT INTO interview (interview_id, created_at, updated_at)
VALUES (1, NOW(), NOW());
INSERT INTO interview (interview_id, created_at, updated_at)
VALUES (2, NOW(), NOW());
INSERT INTO interview (interview_id, created_at, updated_at)
VALUES (3, NOW(), NOW());

INSERT INTO resume (resume_id, member_id, file_name, file_url, file_size, created_at, updated_at)
VALUES (1, 1, 'resume_1.pdf', '/files/resume_1.pdf', 321, NOW(), NOW());


INSERT INTO resume (resume_id, member_id, file_name, file_url, file_size, created_at, updated_at)
VALUES (2, 3, 'resume_2.pdf', '/files/resume_2.pdf', 460, NOW(), NOW());


INSERT INTO resume (resume_id, member_id, file_name, file_url, file_size, created_at, updated_at)
VALUES (3, 1, 'resume_3.pdf', '/files/resume_3.pdf', 462, NOW(), NOW());


INSERT INTO resume (resume_id, member_id, file_name, file_url, file_size, created_at, updated_at)
VALUES (4, 4, 'resume_4.pdf', '/files/resume_4.pdf', 101, NOW(), NOW());


INSERT INTO resume (resume_id, member_id, file_name, file_url, file_size, created_at, updated_at)
VALUES (5, 3, 'resume_5.pdf', '/files/resume_5.pdf', 434, NOW(), NOW());


INSERT INTO coverletter (coverletter_id, member_id, corporate_name, job_name, created_at, updated_at)
VALUES (1, 2, 'Nunez, Hall and', 'Engineer, broad', NOW(), NOW());


INSERT INTO coverletter (coverletter_id, member_id, corporate_name, job_name, created_at, updated_at)
VALUES (2, 4, 'Turner PLC', 'IT sales profes', NOW(), NOW());


INSERT INTO coverletter (coverletter_id, member_id, corporate_name, job_name, created_at, updated_at)
VALUES (3, 3, 'Bennett Group', 'Waste managemen', NOW(), NOW());


INSERT INTO coverletter (coverletter_id, member_id, corporate_name, job_name, created_at, updated_at)
VALUES (4, 5, 'Blanchard LLC', 'Engineer, autom', NOW(), NOW());


INSERT INTO coverletter (coverletter_id, member_id, corporate_name, job_name, created_at, updated_at)
VALUES (5, 5, 'Turner, Hall an', 'Surveyor, miner', NOW(), NOW());


INSERT INTO qna (qna_id, coverletter_id, question, answer, created_at, updated_at)
VALUES (1, 3, 'Program nothing consider order significant.', 'Single turn house partner young us value alone.', NOW(),
        NOW());


INSERT INTO qna (qna_id, coverletter_id, question, answer, created_at, updated_at)
VALUES (2, 4, 'Role eye step author land thought beat quite.', 'Million again box. Campaign place election.
Himself use general in check. West sound somebody.', NOW(), NOW());


INSERT INTO qna (qna_id, coverletter_id, question, answer, created_at, updated_at)
VALUES (3, 1, 'Run practice attention population move call miss available.',
        'Address data last method budget me. Physical class partner church term land big.', NOW(), NOW());


INSERT INTO qna (qna_id, coverletter_id, question, answer, created_at, updated_at)
VALUES (4, 4, 'Per claim service.', 'Key seek set rise follow west. Reveal society involve result compare south.',
        NOW(), NOW());


INSERT INTO qna (qna_id, coverletter_id, question, answer, created_at, updated_at)
VALUES (5, 5, 'Anyone Republican report compare.', 'Than a network day. Brother seem teach organization pull view.',
        NOW(), NOW());


INSERT INTO qna (qna_id, coverletter_id, question, answer, created_at, updated_at)
VALUES (6, 1, 'Will her live.', 'Story everybody spring table poor several its.', NOW(), NOW());


INSERT INTO qna (qna_id, coverletter_id, question, answer, created_at, updated_at)
VALUES (7, 4, 'Chair southern paper now could rate difficult.',
        'Picture value push. Experience second under these whether you mention.', NOW(), NOW());


INSERT INTO qna (qna_id, coverletter_id, question, answer, created_at, updated_at)
VALUES (8, 1, 'Character fast degree today threat fall room order.',
        'Allow purpose public myself. Step message feel. Notice business section.', NOW(), NOW());


INSERT INTO qna (qna_id, coverletter_id, question, answer, created_at, updated_at)
VALUES (9, 5, 'Subject owner born huge short.',
        'Kitchen matter threat Democrat give. From fund analysis necessary reflect all.', NOW(), NOW());


INSERT INTO qna (qna_id, coverletter_id, question, answer, created_at, updated_at)
VALUES (10, 3, 'Possible government per back.',
        'Song series method easy magazine affect around. Education grow question land stay.', NOW(), NOW());


INSERT INTO member_interview (member_interview_id, member_id, interview_id, resume_id, coverletter_id, status,
                              created_at, updated_at)
VALUES (1, 2, 3,
        3, 3,
        'SCHEDULED', NOW(), NOW());


INSERT INTO member_interview (member_interview_id, member_id, interview_id, resume_id, coverletter_id, status,
                              created_at, updated_at)
VALUES (2, 1, 1,
        2, 3,
        'SCHEDULED', NOW(), NOW());


INSERT INTO member_interview (member_interview_id, member_id, interview_id, resume_id, coverletter_id, status,
                              created_at, updated_at)
VALUES (3, 5, 3,
        2, 3,
        'DONE', NOW(), NOW());


INSERT INTO member_interview (member_interview_id, member_id, interview_id, resume_id, coverletter_id, status,
                              created_at, updated_at)
VALUES (4, 4, 1,
        5, 4,
        'SCHEDULED', NOW(), NOW());


INSERT INTO member_interview (member_interview_id, member_id, interview_id, resume_id, coverletter_id, status,
                              created_at, updated_at)
VALUES (5, 3, 1,
        1, 5,
        'NO_SHOW', NOW(), NOW());
