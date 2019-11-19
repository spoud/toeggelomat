package io.spoud.services;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class MatchPointsServiceTest {

  @Test
  public void slopeTest(){
    Assertions.assertEquals((int)(MatchPointsService.slope(1000, 2000) * 10), 5);
    Assertions.assertEquals((int)(MatchPointsService.slope(500, 2000)*10), 0);
    Assertions.assertEquals((int)(MatchPointsService.slope(2000, 2000)*10), 10);
    Assertions.assertEquals((int)(MatchPointsService.slope(1100, 2000)*10), 7);
    Assertions.assertEquals((int)(MatchPointsService.slope(900, 2000)*10), 3);
  }

}
