$(document).ready(function() {

  //
  var root = $(
      "<div class='row'>" +
          "<div class='span3 hidden-phone'>" +
              "<div style='position: fixed;overflow-x: hidden;overflow-y: auto; height: 100%; z-index: 10' id='nav-left'>" +
              "</div>" +
          "</div>" +
          "<div class='span8 offset1' id='nav-right'></div>" +
      "</div>");

  //
  $("div.book > div.toc").appendTo(root.find("#nav-left"));
  $("div.chapter > div.toc").remove();

  //
  $("div.book > div.list-of-figures").remove();
  $("div.book > div.list-of-tables").remove();
  $("div.book > div.list-of-examples").remove();

  //
  $("div.book > div.titlepage").appendTo(root.find("#nav-right"));
  $("div.book > div.preface").appendTo(root.find("#nav-right"));
  $("div.book > div.chapter").appendTo(root.find("#nav-right"));

  // Callout in program listing
  root.find("span.co").each(function() {
    var text = "";
    $(this).find("img").each(function() {
      var index = $(this).attr("alt");
      text = text + index.substring(1, index.length - 1);
    }).remove();
    $(this).text(text);
    $(this).addClass("badge").addClass("nocode").removeClass("co");
  });

  // Replace callout tables with ordered lists
  root.find(".calloutlist").each(function() {
    var ol = $("<ol></ol>").appendTo($(this));
    $(this).find("tr").each(function() {
      var o = $(this).find("td:eq(1)").html();
      $("<li></li>").appendTo(ol).html(o);
    });
    $(this).find("> table").remove();
  });

  //
  root.appendTo("div.book");

});
