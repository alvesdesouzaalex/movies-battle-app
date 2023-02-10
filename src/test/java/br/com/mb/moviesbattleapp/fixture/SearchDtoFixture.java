package br.com.mb.moviesbattleapp.fixture;

import br.com.mb.moviesbattleapp.domain.omdb.SearchDto;

public class SearchDtoFixture {

    public static SearchDto of(String imdbId, String poster, String title, String year) {
        SearchDto searchDto = new SearchDto();
        searchDto.setImdbID(imdbId);
        searchDto.setPoster(poster);
        searchDto.setTitle(title);
        searchDto.setYear(year);
        return searchDto;
    }
}
