package com.mysite25.core.services;

import java.util.List;

public interface SearchService {
    List<String> find(String text1, String text2, List<String> paths);
}
