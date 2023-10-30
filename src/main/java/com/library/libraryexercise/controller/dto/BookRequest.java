package com.library.libraryexercise.controller.dto;

import java.util.Objects;

public class BookRequest {
   private String title;
   private String author;

   public String getTitle() {
      return title;
   }

   public void setTitle(String title) {
      this.title = title;
   }

   public String getAuthor() {
      return author;
   }

   public void setAuthor(String author) {
      this.author = author;
   }

   @Override
   public boolean equals(Object o) {
      if (this == o) {
         return true;
      }
      if (o == null || getClass() != o.getClass()) {
         return false;
      }
      BookRequest that = (BookRequest) o;
      return Objects.equals(title, that.title) && Objects.equals(author, that.author);
   }

   @Override
   public int hashCode() {
      return Objects.hash(title, author);
   }
}


