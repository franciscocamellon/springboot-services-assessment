package br.edu.infnet.assessment.repository;

import br.edu.infnet.assessment.entities.Enrollment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;


public interface EnrollmentRepository extends JpaRepository<Enrollment,Long> {

	Collection<Enrollment> findByCourseIdAndGradeGreaterThanEqual(Long courseId, Double minGrade);

	Collection<Enrollment> findByCourseIdAndGradeLessThan(Long courseId, Double maxExclusive);

}
