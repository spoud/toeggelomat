package io.spoud.services;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class PointsCalculationServiceTest {

  @Test
  public void slopeTest(){
    Assertions.assertEquals((int)(PointsCalculationService.slope(1000, 2000)*10), 5);
    Assertions.assertEquals((int)(PointsCalculationService.slope(500, 2000)*10), 0);
    Assertions.assertEquals((int)(PointsCalculationService.slope(2000, 2000)*10), 10);
    Assertions.assertEquals((int)(PointsCalculationService.slope(1100, 2000)*10), 7);
    Assertions.assertEquals((int)(PointsCalculationService.slope(900, 2000)*10), 3);
  }
}
