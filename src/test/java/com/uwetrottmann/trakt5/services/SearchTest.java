package com.uwetrottmann.trakt5.services;

import com.uwetrottmann.trakt5.BaseTestCase;
import com.uwetrottmann.trakt5.TestData;
import com.uwetrottmann.trakt5.entities.SearchResult;
import com.uwetrottmann.trakt5.enums.IdType;
import com.uwetrottmann.trakt5.enums.Type;
import org.junit.Test;
import retrofit2.Response;

import java.io.IOException;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class SearchTest extends BaseTestCase {

    @Test
    public void test_textQuery_show() throws IOException {
        Response<List<SearchResult>> response = getTrakt().search().textQuery("House", Type.SHOW, null, 1,
                DEFAULT_PAGE_SIZE).execute();
        assertSuccessfulResponse(response);
        List<SearchResult> results = response.body();
        assertThat(results).isNotEmpty();
        for (SearchResult result : results) {
            assertThat(result.score).isPositive();
            assertThat(result.show).isNotNull();
        }
    }

    @Test
    public void test_textQuery_show_withYear() throws IOException {
        Response<List<SearchResult>> response = getTrakt().search().textQuery("Empire", Type.SHOW, 2015, 1,
                DEFAULT_PAGE_SIZE).execute();
        assertSuccessfulResponse(response);
        List<SearchResult> results = response.body();
        assertThat(results).isNotEmpty();
        for (SearchResult result : results) {
            assertThat(result.score).isPositive();
            assertThat(result.show).isNotNull();
        }
    }

    @Test
    public void test_textQuery_movie() throws IOException {
        Response<List<SearchResult>> response = getTrakt().search().textQuery("Tron", Type.MOVIE, null, 1,
                DEFAULT_PAGE_SIZE).execute();
        assertSuccessfulResponse(response);
        List<SearchResult> results = response.body();
        assertThat(results).isNotEmpty();
        for (SearchResult result : results) {
            assertThat(result.score).isPositive();
            assertThat(result.movie).isNotNull();
        }
    }

    @Test
    public void test_textQuery_person() throws IOException {
        Response<List<SearchResult>> response = getTrakt().search().textQuery("Bryan Cranston", Type.PERSON, null, 1,
                DEFAULT_PAGE_SIZE).execute();
        assertSuccessfulResponse(response);
        List<SearchResult> results = response.body();
        assertThat(results).isNotEmpty();
        for (SearchResult result : results) {
            assertThat(result.score).isPositive();
            assertThat(result.person).isNotNull();
        }
    }

    @Test
    public void test_idLookup() throws IOException {
        Response<List<SearchResult>> response = getTrakt().search().idLookup(IdType.TVDB,
                String.valueOf(TestData.SHOW_TVDB_ID), 1,
                DEFAULT_PAGE_SIZE).execute();
        assertSuccessfulResponse(response);
        List<SearchResult> results = response.body();
        // episode and show
        assertThat(results).hasSize(2);
        assertThat(results.get(0).score).isNull();

        response = getTrakt().search().idLookup(IdType.TMDB,
                String.valueOf(TestData.MOVIE_TMDB_ID), 1,
                DEFAULT_PAGE_SIZE).execute();
        assertSuccessfulResponse(response);
        results = response.body();
        // episode, person and movie
        assertThat(results).hasSize(3);
        assertThat(results.get(0).score).isNull();
    }

}
