package com.example.communication.repository;

import com.example.communication.model.Message;
import com.example.communication.model.User;
import com.example.communication.model.dto.MessageDTO;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface MessageRepository extends JpaRepository<Message, Long> {

  @Query("select new com.example.communication.model.dto.MessageDTO(" +
          "   m, " +
          "   count(ml), " +
          "   sum(case when ml = :user then 1 else 0 end) > 0" +
          ") " +
          "from Message m left join m.likes ml " +
          "where m.text like %:filter% " +
          "group by m")
  Iterable<MessageDTO> findByTextContains(@Param("filter") String filter, @Param("user") User user);

  @Query("select new com.example.communication.model.dto.MessageDTO(" +
          "   m, " +
          "   count(ml), " +
          "   sum(case when ml = :user then 1 else 0 end) > 0" +
          ") " +
          "from Message m left join m.likes ml " +
          "where m.user = :author " +
          "group by m order by m.postTime")
  Iterable<MessageDTO> findByUserId(@Param("user") User user, @Param("author") User author);

  @Query("select new com.example.communication.model.dto.MessageDTO(" +
          "   m, " +
          "   count(ml), " +
          "   sum(case when ml = :user then 1 else 0 end) > 0" +
          ") " +
          "from Message m left join m.likes ml " +
          "group by m order by m.postTime")
  List<MessageDTO> findAll(@Param("user") User user);
}
