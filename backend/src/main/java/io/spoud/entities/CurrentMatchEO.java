package io.spoud.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CurrentMatchEO {

    private PlayerEO playerRedDefense;

    private PlayerEO playerRedOffense;

    private PlayerEO playerBlueDefense;

    private PlayerEO playerBlueOffense;
}
