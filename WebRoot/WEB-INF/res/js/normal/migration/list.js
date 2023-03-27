//var xml_data;
//$.get(ctx + "/service/migration/persons", function(data){
//    xml_data = data;
//});

var tl;
function onLoad() {
    var eventSource = new Timeline.DefaultEventSource(0);

    var theme = Timeline.ClassicTheme.create();
    // demonstrate highlighting of labels (in addition to icons and tapes)
    theme.event.highlightLabelBackground = true;
    theme.event.bubble.width = 320;

    var zones = [
        {   start:    "1700",
            end:      "2100",
            magnify:  5,
            unit:     Timeline.DateTime.DECADE
        },
        {   start:    "1800",
            end:      "2100",
            magnify:  3,
            unit:     Timeline.DateTime.YEAR,
            multiple: 5
        }
    ];
    var zones2 = [
        {   start:    "1700",
            end:      "2100",
            magnify:  5,
            unit:     Timeline.DateTime.DECADE
        },
        {   start:    "1800",
            end:      "2100",
            magnify:  3,
            unit:     Timeline.DateTime.YEAR,
            multiple: 10
        }
    ];

    var d = Timeline.DateTime.parseGregorianDateTime("1000");
    var bandInfos = [
        Timeline.createHotZoneBandInfo({
            width:          "75%",
            intervalUnit:   Timeline.DateTime.CENTURY,
            intervalPixels: 250,
            zones:          zones,
            eventSource:    eventSource,
            date:           d,
            timeZone:       -6,
            theme:          theme
        }),
        Timeline.createHotZoneBandInfo({
            width:          "25%",
            intervalUnit:   Timeline.DateTime.CENTURY,
            intervalPixels: 70,
            zones:          zones2,
            eventSource:    eventSource,
            date:           d,
            overview:       true,
            theme:          theme
        })
    ];
    bandInfos[1].syncWith = 0;
    bandInfos[1].highlight = true;

    tl = Timeline.create(document.getElementById("tl"), bandInfos, Timeline.HORIZONTAL);
//    tl.loadXML(ctx + "/service/migration/persons", function(xml, url) {
    tl.loadXML("/res/js/normal/migration/data.json", function(xml, url) {
        eventSource.loadXML(xml, url);
    });

    setupFilterHighlightControls(document.getElementById("controls"), tl, [0,1], theme);
}
function centerTimeline(year) {
    tl.getBand(0).setCenterVisibleDate(new Date(year, 0, 1));
}

var resizeTimerID = null;
function onResize() {
    if (resizeTimerID == null) {
        resizeTimerID = window.setTimeout(function() {
            resizeTimerID = null;
            tl.layout();
        }, 500);
    }
}

function centerSimileAjax(date) {
    tl.getBand(0).setCenterVisibleDate(SimileAjax.DateTime.parseGregorianDateTime(date));
}

function setupFilterHighlightControls(div, timeline, bandIndices, theme) {
    var table = document.createElement("table");
    var tr = table.insertRow(0);

    var td = tr.insertCell(0);
    td.innerHTML = "Filter:";

    td = tr.insertCell(1);
    td.innerHTML = "Highlight:";

    var handler = function(elmt, evt, target) {
        onKeyPress(timeline, bandIndices, table);
    };

    tr = table.insertRow(1);
    tr.style.verticalAlign = "top";

    td = tr.insertCell(0);

    var input = document.createElement("input");
    input.type = "text";
    SimileAjax.DOM.registerEvent(input, "keypress", handler);
    td.appendChild(input);

    for (var i = 0; i < theme.event.highlightColors.length; i++) {
        td = tr.insertCell(i + 1);

        input = document.createElement("input");
        input.type = "text";
        SimileAjax.DOM.registerEvent(input, "keypress", handler);
        td.appendChild(input);

        var divColor = document.createElement("div");
        divColor.style.height = "0.5em";
        divColor.style.background = theme.event.highlightColors[i];
        td.appendChild(divColor);
    }

    td = tr.insertCell(tr.cells.length);
    var button = document.createElement("button");
    button.innerHTML = "Clear All";
    SimileAjax.DOM.registerEvent(button, "click", function() {
        clearAll(timeline, bandIndices, table);
    });
    td.appendChild(button);

    div.appendChild(table);
}

var timerID = null;
function onKeyPress(timeline, bandIndices, table) {
    if (timerID != null) {
        window.clearTimeout(timerID);
    }
    timerID = window.setTimeout(function() {
        performFiltering(timeline, bandIndices, table);
    }, 300);
}
function cleanString(s) {
    return s.replace(/^\s+/, '').replace(/\s+$/, '');
}
function performFiltering(timeline, bandIndices, table) {
    timerID = null;

    var tr = table.rows[1];
    var text = cleanString(tr.cells[0].firstChild.value);

    var filterMatcher = null;
    if (text.length > 0) {
        var regex = new RegExp(text, "i");
        filterMatcher = function(evt) {
            return regex.test(evt.getText()) || regex.test(evt.getDescription());
        };
    }

    var regexes = [];
    var hasHighlights = false;
    for (var x = 1; x < tr.cells.length - 1; x++) {
        var input = tr.cells[x].firstChild;
        var text2 = cleanString(input.value);
        if (text2.length > 0) {
            hasHighlights = true;
            regexes.push(new RegExp(text2, "i"));
        } else {
            regexes.push(null);
        }
    }
    var highlightMatcher = hasHighlights ? function(evt) {
        var text = evt.getText();
        var description = evt.getDescription();
        for (var x = 0; x < regexes.length; x++) {
            var regex = regexes[x];
            if (regex != null && (regex.test(text) || regex.test(description))) {
                return x;
            }
        }
        return -1;
    } : null;

    for (var i = 0; i < bandIndices.length; i++) {
        var bandIndex = bandIndices[i];
        timeline.getBand(bandIndex).getEventPainter().setFilterMatcher(filterMatcher);
        timeline.getBand(bandIndex).getEventPainter().setHighlightMatcher(highlightMatcher);
    }
    timeline.paint();
}
function clearAll(timeline, bandIndices, table) {
    var tr = table.rows[1];
    for (var x = 0; x < tr.cells.length - 1; x++) {
        tr.cells[x].firstChild.value = "";
    }

    for (var i = 0; i < bandIndices.length; i++) {
        var bandIndex = bandIndices[i];
        timeline.getBand(bandIndex).getEventPainter().setFilterMatcher(null);
        timeline.getBand(bandIndex).getEventPainter().setHighlightMatcher(null);
    }
    timeline.paint();
}

$("#map_query").click(function(){
    self.location='/service/map';
});

$("#mig_query").click(function(){
    self.location='/service/migration/track';
});

$("#timeline_query").click(function(){
    self.location='/service';
});