package project.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import project.beans.Image;

public interface ImageRepository extends JpaRepository<Image, Long> {

	Image findByImageid(Long id);
}
