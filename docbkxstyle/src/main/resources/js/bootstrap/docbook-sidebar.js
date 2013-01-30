$(document).ready(function() {

  //
  var row = $(
      "<div class='row'>" +
          "<div class='span3 hidden-phone'>" +
              "<div style='position: fixed;overflow-x: hidden;overflow-y: auto; height: 100%; z-index: 10' id='nav-left'>" +
              "</div>" +
          "</div>" +
          "<div class='span8 offset1' id='nav-right'></div>" +
      "</div>");

  //
  $("div.book > div.toc").appendTo(row.find("#nav-left"));
  $("div.chapter > div.toc").remove();

  //
  $("div.book > div.list-of-figures").remove();
  $("div.book > div.list-of-tables").remove();
  $("div.book > div.list-of-examples").remove();

  //
  $("div.book > div.titlepage").appendTo(row.find("#nav-right"));
  $("div.book > div.preface").appendTo(row.find("#nav-right"));
  $("div.book > div.chapter").appendTo(row.find("#nav-right"));

  //
  row.appendTo("div.book");

});
