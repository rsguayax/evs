----------------------------------------------------------------------------------
--       Reporte Usuarios - Asignaturas - Tests de un evento de evaluación      --
----------------------------------------------------------------------------------
SELECT us.identification, mt.name, mt.code, t.name, t.externalid from matter_test_student mts 
inner join evaluation_assignment_matter eam ON mts.evaluationassignmentmatter_id=eam.id 
inner join evaluation_event_matter_test eemt ON mts.evaluationeventmattertest_id=eemt.id 
inner join test t ON eemt.test_id=t.id
inner join evaluation_event_matter eem ON eam.evaluationeventmatter_id=eem.id
inner join matter mt ON eem.matter_id=mt.id
inner join evaluation_assignment ea ON eam.evaluationassignment_id=ea.id
inner join evs_user us ON ea.user_id=us.id
where eem.evaluationevent_id>=84982 
