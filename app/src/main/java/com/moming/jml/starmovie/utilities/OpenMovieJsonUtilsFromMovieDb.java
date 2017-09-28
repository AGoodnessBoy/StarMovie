package com.moming.jml.starmovie.utilities;

/**
 * Created by admin on 2017/9/28.
 */

/*
{
  "adult": false,
  "backdrop_path": "/fCayJrkfRaCRCTh8GqN30f8oyQF.jpg",
  "belongs_to_collection": null,
  "budget": 63000000,
  "genres": [
    {
      "id": 18,
      "name": "Drama"
    }
  ],
  "homepage": "",
  "id": 550,
  "imdb_id": "tt0137523",
  "original_language": "en",
  "original_title": "Fight Club",
  "overview": "A ticking-time-bomb insomniac and a slippery soap salesman channel primal male aggression into a shocking new form of therapy. Their concept catches on, with underground \"fight clubs\" forming in every town, until an eccentric gets in the way and ignites an out-of-control spiral toward oblivion.",
  "popularity": 0.5,
  "poster_path": null,
  "production_companies": [
    {
      "name": "20th Century Fox",
      "id": 25
    }
  ],
  "production_countries": [
    {
      "iso_3166_1": "US",
      "name": "United States of America"
    }
  ],
  "release_date": "1999-10-12",
  "revenue": 100853753,
  "runtime": 139,
  "spoken_languages": [
    {
      "iso_639_1": "en",
      "name": "English"
    }
  ],
  "status": "Released",
  "tagline": "How much can you know about yourself if you've never been in a fight?",
  "title": "Fight Club",
  "video": false,
  "vote_average": 7.8,
  "vote_count": 3439
}

 */

public final class OpenMovieJsonUtilsFromMovieDb {
    //the movie db api json 参数
    final static String MOVIE_ID="id"; //电影ID
    final static String MOVIE_TITLE="title";//电影标题
    final static String MOVIE_IMG_PATH="backdrop_path";//电影图片
    final static String MOVIE_VOTE="vote_average";//电影评分
    final static String MOVIE_OVERVIEW="overview";//电影简介
    final static String MOVIE_TYPE="genres";//电影类型
    final static String MOVIE_COMPANY="production_companies";//电影公司
    final static String MOVIE_STATUS="status";//发行状态
    final static String MOVIE_RELEASE_DATE="release_date";//发行时间
    final static String MOVIE_RUNTIME="runtime";//电影时长;
    final static String MOVIE_REVENUE="revenue";//票房；




}
