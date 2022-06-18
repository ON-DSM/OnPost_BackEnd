package com.onpost.domain.repository;

import com.onpost.domain.entity.Post;
import com.onpost.domain.repository.custom.CustomPostRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PostRepository extends JpaRepository<Post, Long>, CustomPostRepository {

    @Query("select p from Post p where p.id = :id")
    Optional<Post> findOne(@Param("id") Long id);

    @Query("select p from Post p left join p.writer left join p.postLike where p.id = :id")
    Optional<Post> findOneWithAll(@Param("id") Long id);

    @Query("select p from Post p left join p.comments where p.id = :id")
    Optional<Post> findOneWithComment(@Param("id") Long id);

    @Query("select p from Post p left join p.postLike where p.id = :id")
    Optional<Post> findOneWithLike(@Param("id") Long id);

    @Query(nativeQuery = true
            ,value = "select if(exists(" +
            "select * from post_like p where p.post_id = :id and p.user_id = (" +
            "select m.user_id from member m where m.email = :email)) = 1, 'false', 'true') as result")
    boolean checkLike(@Param("email") String email, @Param("id") Long postId);
}
