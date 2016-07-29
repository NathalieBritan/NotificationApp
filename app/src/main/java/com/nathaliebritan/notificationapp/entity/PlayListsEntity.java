package com.nathaliebritan.notificationapp.entity;

import java.util.List;

/**
 * Created by Nathalie Britan on 27.07.2016.
 */
public class PlayListsEntity {
    public String kind ;
    public String etag ;
    public PageInfo pageInfo;
    public List<Item> items;

    public List<Item> getItems() {
        return items;
    }

    public class PageInfo
    {
        private int totalResults ;
        private int resultsPerPage;

    }
    public class Default
    {
        private String url ;
        private int width ;
        private int height ;

    }
    public class Medium
    {
        private String url;
        private int width ;
        private int height ;

    }
    public class High
    {
        private String url;
        private int width;
        private int height;

    }
    public class Standard
    {
        private String url ;
        private int width ;
        private int height ;

    }
    public class Maxres
    {
        private String url;
        private int width;
        private int height;

    }
    public class Thumbnails
    {
        private Default aDefault;
        private Medium medium;
        private High high;
        private Standard standard ;
        private Maxres maxres ;

    }
    public class Localized
    {
        public String title ;
        public String description ;

    }
    public class Snippet
    {
        private String publishedAt;
        private String channelId;
        private String title ;
        private String description ;
        private Thumbnails thumbnails;
        private String channelTitle;
        private Localized localized;

    }
    public class ContentDetails
    {
        private int itemCount ;

        public int getItemCount() {
            return itemCount;
        }
    }
    public class Item
    {
        private String kind;
        private String etag ;
        private String id ;
        private Snippet snippet;
        public ContentDetails contentDetails;

        public ContentDetails getContentDetails() {
            return contentDetails;
        }
        public String getKind() {
            return kind;
        }
    }
}
