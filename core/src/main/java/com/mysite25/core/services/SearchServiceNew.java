package com.mysite25.core.services;


import com.mysite25.core.models.SearchItem;

import java.util.List;

public interface SearchServiceNew {
    List<SearchItem> performSearch(String path, String searchText, String searchEngine);
}
