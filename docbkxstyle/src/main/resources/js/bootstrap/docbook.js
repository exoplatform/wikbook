$(document).ready(function() {

  //
  var addModal = function(id, heading, elements, bodyClass) {
    // The modal window
    var modal = $("<div id='" + id + "' class='modal hide'>" +
        "<div class='modal-header'>" +
        "<a href='#' class='close'>&times;</a>" +
        "<h3>" + heading + "</h3>" +
        "</div>" +
        "<div class='modal-body'></div>" +
        "<div class='modal-footer'></div>" +
        "</div>");
    if (bodyClass != null) {
      modal.find(".modal-body").addClass(bodyClass);
    }
    $("div.book").before(modal);
    var body = modal.find(".modal-body");
    elements.appendTo(body);
    body.find("a").click(function() { modal.modal("hide"); });
  };

  //
  var addModalToBar = function(id, heading) {
    $("<li>" +
        "<a href='#" + id + "' data-toggle='modal' data-backdrop='true' data-keyboard='true'>" + heading + "</a>" +
        "</li>").appendTo("#primarynav");
  };

  //
  var title = $("div.titlepage h1.title");
  if (title.length == 1) {
    var titleLink = $("#topbar");
    titleLink.text(title.text());
    title.remove();
  }

  // Table of Content
  if ($('div.book > div.toc').length == 1) {
    addModal("modal-toc", "Table of Content", $("div.book > div.toc > dl").addClass("unstyled").clone(), "modal-overflow");
    addModalToBar("modal-toc", "Table of Content");
    $(".toc").remove();
  }

  // Lit of Figures
  if ($('div.book > div.list-of-figures').length == 1) {
    addModal("modal-figures", "List of Figures", $("div.book > div.list-of-figures > dl").addClass("unstyled").clone(), "modal-overflow");
    addModalToBar("modal-figures", "List of Figures");
    $(".list-of-figures").remove();
  }

  // List of Tables
  if ($('div.book > div.list-of-tables').length == 1) {
    addModal("modal-tables", "List of Tables", $("div.book > div.list-of-tables > dl").addClass("unstyled").clone(), "modal-overflow");
    addModalToBar("modal-tables", "List of Tables");
    $(".list-of-tables").remove();
  }

  // List of Examples
  if ($('div.book > div.list-of-examples').length == 1) {
    addModal("modal-examples", "List of Examples", $("div.book > div.list-of-examples > dl").addClass("unstyled").clone(), "modal-overflow");
    addModalToBar("modal-examples", "List of Examples");
    $(".list-of-examples").remove();
  }

  // Callout
  $(".calloutlist").each(function() {
    var ol = $("<ol></ol>").appendTo($(this));
    $(this).find("tr").each(function() {
      var o = $(this).find("td:eq(1)").html();
      $("<li></li>").appendTo(ol).html(o);
    });
    $(this).find("> table").remove();
  });
});
