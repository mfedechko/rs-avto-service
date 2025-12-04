package com.rsavto.categories.data;

/**
 * @author Mykola Fedechko
 */
public class PartPage {

    private final String pageLink;
    private final String imageLink;

    public PartPage(final String pageLink, final String imageLink) {
        this.pageLink = pageLink;
        this.imageLink = imageLink;
    }

    public String getPageLink() {
        return pageLink;
    }

    public String getImageLink() {
        return imageLink;
    }
}
