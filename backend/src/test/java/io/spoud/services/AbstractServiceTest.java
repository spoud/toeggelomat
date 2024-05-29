package io.spoud.services;

import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.AfterEach;


@Transactional(Transactional.TxType.NOT_SUPPORTED)
public class AbstractServiceTest {

  @Inject
  EntityManager entityManager;

  @AfterEach
  @Transactional
  final void tearDownAbstract() {
    final String sql = "TRUNCATE t_match, t_player;";

    entityManager.createNativeQuery(sql).executeUpdate();
  }


}
