package com.example.vic.pamlab3;

/**
 * Created by vic on 1/3/18.
 */



public class Novelty
{
    private int id;
    private String imageUri;
    private String title;
    private String link;
    private String description;
    private String created_at;

    public Novelty()
    {
        this.imageUri = "";
        this.title = "";
        this.link = "";
        this.description = "";
    }
    public Novelty(String imageUri, String title, String link, String description)
    {
        this.imageUri = imageUri;
        this.title = title;
        this.link = link;
        this.description = description;
    }

    public int getId()
    {
        return id;
    }

    public String getImageUri()
    {
        return imageUri;
    }

    public String getTitle()
    {
        return title;
    }

    public String getLink()
    {
        return link;
    }

    public String getCreated_at()
    {
        return created_at;
    }



    public void setId(int id)
    {
        this.id = id;
    }
    public String getDescription()
    {
        return description;
    }

    public void setImageUri(String imageUri)
    {
        this.imageUri = imageUri;
    }

    public void setTitle(String title)
    {
        this.title = title;
    }

    public void setLink(String link)
    {
        this.link = link;
    }

    public void setDescription(String description)
    {
        this.description = description;
    }

    public void setCreated_at(String created_at)
    {
        this.created_at = created_at;
    }

    @Override
    public String toString()
    {
        return "Novelty{" +
                "id=" + id +
                ", imageUri='" + imageUri + '\'' +
                ", title='" + title + '\'' +
                ", link='" + link + '\'' +
                ", description='" + description + '\'' +
                ", created_at='" + created_at + '\'' +
                '}';
    }
}
