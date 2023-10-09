package ge.market.flea.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class SearchState {
    private String searchText;
    private String page;
}
