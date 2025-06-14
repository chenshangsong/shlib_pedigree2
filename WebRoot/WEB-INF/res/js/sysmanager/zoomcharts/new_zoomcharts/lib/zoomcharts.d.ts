declare module ZoomCharts.Configuration {
    /* tslint:disable */

    export interface IGeoRectangle {
        east: number;
        north: number;
        south: number;
        west: number;
    }
    export interface BaseApi {
        /** Adds the given data to whatever data the chart has currently loaded. The chart will automatically be updated
        to display this new data if it falls within the currently visible bounds. */
        addData(data: BaseDataObjectBase, sourceId?: string): void;
        /** 
        @deprecated use settings.parentChart instead. */
        addSubchartContainer(container: HTMLElement): void;
        back(): BaseApi;
        clearHistory(): BaseApi;
        /** Applies one of the built-in themes to the chart. This is an alternative to calling 
        `updateSettings({ theme: ZoomCharts.$this.themes.dark })`. */
        customize(
            /** The name of the theme to be applied, must be one of the values defined in the `$this.themes` static property. */
            name: string): BaseApi;
        /** Launches a file download that contains an image of the current state of the chart. */
        export(type: string, dimensions: BaseExportDimensions, transparent: boolean): void;
        /** Returns the dimensions for the image exported with `exportImageAsString`.
        @deprecated use `exportImageGetDimensions` instead */
        exportGetDimensions(dimensions: BaseExportDimensions): {
                width: number;
                height: number;
                scale: number;
                chartWidth: number;
                chartHeight: number;
            };
        /** Saves the current chart state as an image. */
        exportImageAsString(type: string, dimensions: BaseExportDimensions, transparent: boolean): string;
        /** Returns the dimensions for the image exported with `exportImageAsString`. */
        exportImageGetDimensions(dimensions: BaseExportDimensions): {
                width: number;
                height: number;
                scale: number;
                chartWidth: number;
                chartHeight: number;
            };
        fullscreen(isFullscreen: boolean): boolean;
        home(): boolean;
        /** Removes an event listener that was added by a call to `on` or by specifying it in settings.
        Note that the listener must be the exact same reference, which means that anonymous functions should not be used in call to `on`. */
        off(
            /** the type of the event. Please see the documentation for `on` about valid values. */
            name: string, listener: (event: BaseMouseEvent, args: BaseChartEventArguments) => void): boolean;
        /** Adds event listener. */
        on(
            /** The type of the event for which the listener will be added. See method overloads for valid values. */
            name: string, 
            /** The callback function. It receives two arguments - the mouse event data and a separate object containing chart specific information. */
            listener: (event: BaseMouseEvent, args: BaseChartEventArguments) => void): void;
        /** Adds an event listener for when the current view has changed (usually after panning and navigation). */
        on(name: "chartUpdate", listener: (event: BaseMouseEvent, args: BaseChartEventArguments) => void): void;
        /** Adds an event listener for the mouse click (or touch tap) event. */
        on(name: "click", listener: (event: BaseMouseEvent, args: BaseChartEventArguments) => void): void;
        /** Adds an event listener for the mouse double click (or touch double tap) event. */
        on(name: "doubleClick", listener: (event: BaseMouseEvent, args: BaseChartEventArguments) => void): void;
        /** Adds an event listener for the mouse click (or touch tap) event. */
        on(name: "error", listener: (
                /** An empty mouse event. */
                event: BaseMouseEvent, args: BaseChartErrorEventArguments) => void): void;
        /** Adds an event listener for when the currently hovered item has changed. */
        on(name: "hoverChange", listener: (event: BaseMouseEvent, args: BaseChartEventArguments) => void): void;
        /** Adds an event listener for when chart placement on screen changes. Note that this is called on every animation frame. */
        on(name: "positionChange", listener: (event: BaseMouseEvent, args: BaseChartEventArguments) => void): void;
        /** Adds an event listener for the mouse right click (or touch longpress) event. */
        on(name: "rightClick", listener: (event: BaseMouseEvent, args: BaseChartEventArguments) => void): void;
        /** Adds an event listener for when the currently selected item or items have changed. */
        on(name: "selectionChange", listener: (event: BaseMouseEvent, args: BaseChartEventArguments) => void): void;
        /** Adds an event listener for when the settings are updated through the API. */
        on(name: "settingsChange", listener: (event: BaseMouseEvent, args: BaseChartSettingsChangeEventArguments) => void): void;
        /** Adds an event listener for the mouse triple click (or touch triple tap) event. */
        on(name: "tripleClick", listener: (event: BaseMouseEvent, args: BaseChartEventArguments) => void): void;
        /** Does immediate repaint. Use to sync updates between multiple charts. */
        paintNow(force?: boolean): BaseApi;
        profiler(): BaseProfiler;
        /** Clears data cache and loads new data. The current view is preserved. */
        reloadData(sourceId?: string): void;
        /** Removes chart from DOM. Is automatically called when you create a new Chart with the same container. */
        remove(): void;
        removeData(data: BaseDataObjectBase, sourceId?: string): void;
        replaceData(data: BaseDataObjectBase, sourceId?: string): void;
        /** Updates the chart settings but instead of merging some settings that are arrays or dictionaries (such as `data`)
        these collections are replaced completely. For example, this allows removal of series or value axis within TimeChart. */
        replaceSettings(changes: BaseSettings): BaseApi;
        restoreState(state: string, animate?: boolean): void;
        /** Saves the current chart state as an image.
        @deprecated use `exportImageAsString` instead */
        saveAsImage(type: string, dimensions: BaseExportDimensions, transparent: boolean): string;
        saveState(): string;
        /** Gets the name of the chart the JavaScript object references. For example 'PieChart' or 'TimeChart'. This field is read-only. */
        typeName: string;
        /** Re-evaluate data filters on next paint. */
        updateFilters(): void;
        /** Updates the chart settings. Only the settings that have to be changed should be passed. Note that some arrays
        and dictionaries (such as `data`) are merged by the ID values - if instead they should be replaced, use
        [`replaceSettings()`](#doc_replaceSettings) method. */
        updateSettings(changes: BaseSettings): BaseApi;
        /** Call when the container size has been changed to update the chart. */
        updateSize(): BaseApi;
        /** Re-evaluate style for all objects on next paint. */
        updateStyle(): void;
    }
    /** Describes the base properties shared between all events raised by the different charts. */
    export interface BaseChartErrorEventArguments extends BaseChartEventArguments {
        /** Any additional arguments that were passed to the error handler. */
        arg: any;
        /** The error message. */
        message: string;
    }
    /** Describes the base properties shared between all events raised by the different charts. */
    export interface BaseChartEventArguments {
        /** The chart object for which the event has been raised. */
        chart: BaseApi;
        origin: string;
    }
    /** Describes the base properties shared between all events raised by the different charts. */
    export interface BaseChartSettingsChangeEventArguments extends BaseChartEventArguments {
        changes?: BaseSettings;
    }
    export interface BaseDataErrorResponse extends BaseDataObjectBase {
        /** If the data could not be retrieved, this field can be used to instruct the chart to cancel the data request and log an error.
        Note that in most cases this field should not be used in favor of returning the correct HTTP status code (such as 404 or 500) so that
        the browser and any proxies do not cache the response. */
        error?: string;
    }
    export interface BaseDataObjectBase {
        /** Store any additional data values within this field. Although it is possible to extend the data object itself with additional fields 
        it is not recommended to do so because a future ZoomCharts version could introduce a known parameter with the same name thus changing the
        behavior of an existing chart. */
        extra?: any;
    }
    export interface BaseExportDimensions {
        /** Only applies when `unit === mm` */
        dpi?: number;
        height?: number;
        scaling?: number;
        /** Valid values: `mm` or `px`. Everything else is treated as `px`. */
        unit?: string;
        width?: number;
    }
    export interface BaseSettingsClassMap {
    }
    export interface BaseLabel {
        align: string;
        angle: number;
        aspectRatio: number;
        backgroundStyle: BaseSettingsBackgroundStyle;
        borderRadius: number;
        /** The bounds from where the label was last rendered. */
        currentBounds: BaseRect;
        hheight: number;
        hwidth: number;
        image: string;
        imageSlicing: [number, number, number, number];
        lineSpacing: number;
        margin: number;
        maxWidth: number;
        padding: number;
        reset(settings?: BaseSettingsLabelStyle, configMapping?: BaseSettingsClassMap, configPath?: string): void;
        text: string;
        textStyle: BaseSettingsTextStyle;
        visible: boolean;
    }
    /** Represents a single pointer. On multitouch, separate event for each pointer will be fired. */
    export interface BaseMouseEvent {
        altKey: boolean;
        changedPointerCount: number;
        consumed: boolean;
        ctrlKey: boolean;
        cursor: string;
        defaultPrevented: boolean;
        distance(p: {
                x: number;
                y: number;
            }): number;
        dx: number;
        dy: number;
        identifier: string;
        isRightMB: boolean;
        /** Verifies if the original event described by this instance is within a specified distance of the given position. */
        isWithinDistance(
            /** The mouse event/pointer to which this instance is compered */
            current: {
                pageX: number;
                pageY: number;
                timeStamp: number;
            }, 
            /** The distance, in pixels, non-inclusive, below which the method will return positive */
            dist: number, 
            /** Time, in milliseconds. If specified, also verifies that the original event did not occur too long ago. */
            maxAge?: number): boolean;
        pageX: number;
        pageY: number;
        pressed: boolean;
        preventDefault(): void;
        shiftKey: boolean;
        /** only on up event */
        swipeDown: boolean;
        /** only on up event */
        swipeLeft: boolean;
        /** only on up event */
        swipeRight: boolean;
        swipeSpeed: number;
        /** only on up event */
        swipeUp: boolean;
        target: HTMLElement;
        timeStamp: number;
        touch: boolean;
        vx: number;
        vy: number;
        wheelx: number;
        wheely: number;
        x: number;
        y: number;
    }
    export interface BaseProfiler {
        hasPendingRequests(): boolean;
        measureFps(measureFpsIters: number, measureFpsCallback: (fps: number, iterations: number, time: number) => void): boolean;
    }
    export interface BaseRect {
        addBounds(x0: number, y0: number, x1: number, y1: number): BaseRect;
        addLine(lineArray: Array<number>): BaseRect;
        addPoint(x: number, y: number): BaseRect;
        addRect(rect: BaseRect): BaseRect;
        area(): number;
        clip(rect: BaseRect): BaseRect;
        clone(): BaseRect;
        containsPoint(x: number, y: number): boolean;
        equals(b: BaseRect): boolean;
        h(): number;
        inflate(scale: number): BaseRect;
        intersectsSegment(x0: number, y0: number, x1: number, y1: number): boolean;
        isEmpty(): boolean;
        isInside(rect: BaseRect): boolean;
        isOutside(rect: BaseRect): boolean;
        moveBy(x: number, y: number): BaseRect;
        overlaps(bounds: BaseRect): boolean;
        overlapsRect(x0: number, y0: number, x1: number, y1: number): boolean;
        toString(): string;
        translate(txm: number, txa: number, tym: number, tya: number): BaseRect;
        w(): number;
        x0: number;
        x1: number;
        y0: number;
        y1: number;
    }
    export interface BaseSettings {
        advanced?: BaseSettingsAdvanced;
        /** Chart area related settings. */
        area?: BaseSettingsArea;
        /** The URL root where the ZoomCharts library and assets are located. For example, if the base.css file is available at
        'http://server/css/zoomcharts/zc.css' then this value should be set to 'http://server/css/zoomcharts/'.
        Note that the library will try to determine its location automatically by searching the included script tags.
        So this property can be skipped if the assets folder is located next to 'zoomcharts.js' file on the server. */
        assetsUrlBase?: string;
        /** Element of the page where the chart will be inserted. Any HTML element should work, for example you can use a `<div>`. 
        
        Any contents of the element will be cleared - this behavior can be used to specify a loading message as the initial content,
        for example `<div>Chart is being initialized...</div>`.
        
        Note that a single element can host only one chart. If the same container is given to another chart, the previous chart will
        be automatically disposed.
        Unless `parentChart` is specified, the value of the property is mandatory and can only be specified while creating the chart, 
        not with `updateSettings`. The value can be either an ID of an existing element or a reference to a DOM element. */
        container?: (string|HTMLElement);
        /** Settings for displaying chart credits. Use it as a reference to additional data sources if necessery.
        ![Chart including credits](images/settings-credits.png)
        Note that even if credits enabled on page load, it's possible to hide on exported images. */
        credits?: BaseSettingsCredits;
        /** Settings used to load data into chart. Customise preferred data source feeding methods.
        You can use one of these options: url, data function, preloaded data. */
        data?: Array<BaseSettingsData>;
        /** The events used to handle user interaction with UI elements. */
        events?: BaseSettingsEvents<BaseChartEventArguments, BaseChartEventArguments>;
        /** Customise chart resize handles or animation duration settings. */
        interaction?: BaseSettingsInteraction;
        legend?: BaseSettingsLegend;
        /** Localizeable strings including export type options and useful default buttons used for chart interaction.
        Buttons like to navigate back, set the chart on full screen and others. */
        localization?: BaseSettingsLocalization;
        /** The parent chart within which the new chart will be rendered. If this property is specified, `container` cannot be
        specified.
        
        Use `area.left`, `area.top`, `area.width` and `area.height` settings to position the subchart within parent chart. */
        parentChart?: BaseApi;
        /** Theme to apply. You can either use this to share configuration objects between multiple charts or use one of the predefined
        themes. */
        theme?: BaseSettings;
        /** The chart's main title. */
        title?: BaseSettingsTitle;
        /** Adjustable settings to manage default and custom toolbar items, as well as toolbar overall appearance. */
        toolbar?: BaseSettingsToolbar;
    }
    export interface BaseSettingsAdvanced {
        /** List of assets to load from assets directory. This should be used to load CSS files. */
        assets?: Array<string>;
        /** URL for export proxy requests. */
        exportProxyURL?: string;
        /** Enables high resolution rendering on high DPI screens. As performance is varied across different browsers, disable this to
        improve the performance of your application. 
        
        Unlike some other frameworks, this setting does not force a constant 2x scaling on the chart,
        instead when this is `true`, it renders according to the browser DPI setting. */
        highDPI?: boolean;
        /** Whether to store entire label into bitmap. Use it to improve the performance of your application. */
        labelCache?: boolean;
        /** Whether to show verbose logging. */
        logging?: boolean;
        /** Maximum height of canvas object. The canvas will be stretched if chart is bigger that this. Note that increasing beyond 2048
        is known to cause loss of hardware acceleration on Safari/OSX. */
        maxCanvasHeight?: number;
        /** Maximum width of canvas object. The canvas will be stretched if chart is bigger that this. Note that increasing beyond 2048
        is known to cause loss of hardware acceleration on Safari/OSX. */
        maxCanvasWidth?: number;
        /** Extra assets to be loaded for pdfExport action. */
        pdfExportAssets?: Array<string>;
        /** Pointer related settings. */
        pointer?: BaseSettingsAdvancedPointer;
        /** Whether to show frames per second on the chart. */
        showFPS?: boolean;
        /** Whether to show the current timestamp on the chart. Use only for debugging. */
        showTimestamp?: boolean;
        /** Whether to show the trail of each touch pointer and also display pointers that are no longer active. 
        For this setting to have effect, `showTouches` must be `true`. */
        showTouchTrail?: boolean;
        /** Whether to use debugging option to paint pointer trails on screen. */
        showTouches?: boolean;
        /** Miscellaneous style settings. */
        style?: BaseSettingsAdvancedStyle;
        /** CSS class for current theme. Used to reference chart container in CSS files. */
        themeCSSClass?: string;
        /** Whether to use requestAnimationFrame for requested paint instead of setTimeout. */
        useAnimationFrame?: boolean;
    }
    export interface BaseSettingsAdvancedPointer {
        /** Pixels pointer can move around and still be registered as a click. */
        clickSensitivity?: number;
        /** Pixels pointer can move around and still be registered as double click. */
        doubleClickSensitivity?: number;
        /** Maximum time in ms between clicks to register a double click. */
        doubleClickTimeout?: number;
        /** The distance in pixels the pointer is allowed to be moved before the long-press event is cancelled. */
        longPressSensitivity?: number;
        /** Time in ms the pointer has to be hold to register a long press (an alternative to clicking the right mouse button). */
        longPressTimeout?: number;
        /** If enabled, normal click event is not sent when user performs a double click. A not so nice side effect is that any on click
        actions are delayed by the double click timeout. Set to false if you are not relying on double click events. */
        noClickOnDoubleClick?: boolean;
        /** Time window to use for pointer speed estimation. */
        speedAveragingPeriod?: number;
    }
    export interface BaseSettingsAdvancedStyle {
        /** Loading arc animation style */
        loadingArcStyle?: {
            lineColor?: string;
            lineWidth?: number;
            /** Specifies the location of the loading indicator. */
            location?: string;
            /** Loading arc radius. */
            r?: number;
        };
        /** Message text */
        messageTextStyle?: {
            fillColor?: string;
            font?: string;
        };
    }
    export interface BaseSettingsArea {
        /** Height of the chart. If undefined the chart height will adapt to container element. */
        height?: number;
        /** The horizontal position of the chart. This setting only applies when this chart is nested within another by using `parentChart` setting. */
        left?: number;
        /** The maximum chart height. Chart will not resize below this. */
        maxHeight?: number;
        /** The maximum chart width. The chart will not resize below this */
        maxWidth?: number;
        /** The minimum chart height. Chart will not resize below this. */
        minHeight?: number;
        /** The minimum chart width. The chart will not resize below this */
        minWidth?: number;
        /** Area style. */
        style?: BaseSettingsAreaStyle;
        /** The vertical position of the chart. This setting only applies when this chart is nested within another by using `parentChart` setting. */
        top?: number;
        /** Width of the chart. If undefined the chart width will adapt to container element. */
        width?: number;
    }
    export interface BaseSettingsAreaStyle {
        /** Background fill color of chart area */
        fillColor?: string;
        /** Background image of  chart area. */
        image?: string;
        /** The foreground fill color of the chart area. `rgba()` with alpha transparency should be used */
        overlayColor?: string;
    }
    export interface BaseSettingsBackgroundStyle extends BaseSettingsLineStyle {
        fillColor?: (string|CanvasGradient);
        shadowBlur?: number;
        shadowColor?: string;
        shadowOffsetX?: number;
        shadowOffsetY?: number;
    }
    export interface BaseSettingsChartPanel {
        /** Panel alignment */
        align?: string;
        /** Whether allow packing over other panels */
        floating?: boolean;
        /** The location of the panel */
        location?: string;
        /** Margin around the panel */
        margin?: number;
        /** Panel side */
        side?: string;
    }
    export interface BaseSettingsCredits {
        /** Enable/disable chart credits. Note that it does not affect exported image. */
        enabled?: boolean;
        /** Whether to render credits on the exported image. Note that it does not affect chart. */
        enabledOnExport?: boolean;
        /** URL of credits image. */
        image?: string;
        /** Image scaling. Use to embed higher resolution images. */
        imageScaling?: number;
        /** Credits location */
        location?: string;
        /** URL to open on click. */
        url?: string;
    }
    export interface BaseSettingsData {
        /** Data loading format. Currently only JSON supported. */
        format?: string;
        /** Data id used for series to reference specific data source. */
        id?: string;
        /** Max number of parallel data requests to issue. More requests will result in faster loading, but might put heavy load on server. */
        numberOfParallelRequests?: number;
        /** Delegate that can be used to process data returned by the server for the HTTP request issued by the chart. */
        postprocessorFunction?: (
            /** The raw data received from the server. */
            data: string) => string;
        /** Provides the ability to embed chart data directly into the chart configuration.
        Data can be represented by an JavaScript object or a JSON string. */
        preloaded?: (string|BaseDataErrorResponse);
        /** Timeout in milliseconds for data requests. This timeout only applies to HTTP requests issued by the chart directly. */
        requestTimeout?: number;
        /** URL to load more data. URL parameters: from, to, unit */
        url?: string;
        /** List of extra parameters to pass with data URL. */
        urlParameters?: Array<{
                /** Parameter name. */
                name?: string;
                /** Parameter value. */
                value?: string;
            }>;
    }
    export interface BaseSettingsEvents<TArguments extends BaseChartEventArguments, TClickArguments extends BaseChartEventArguments> {
        /** Time to wait after last action before firing onChartUpdate event. */
        chartUpdateDelay?: number;
        /** Function called when whenever current view has changed. Usually after panning and navigation. Use to update any linked views.
        Note that this is also fired after chart initialization and API methods. Use args.origin field to determine event's origin. */
        onChartUpdate?: (
            /** An empty mouse event. */
            event: BaseMouseEvent, args: TArguments) => void;
        /** Function called when user clicks on chart. */
        onClick?: (
            /** The mouse event. */
            event: BaseMouseEvent, args: TClickArguments) => void;
        /** Function called when user double clicks on chart. */
        onDoubleClick?: (
            /** The mouse event. */
            event: BaseMouseEvent, args: TClickArguments) => void;
        /** Function called when error occurs, default behavior is log to console. */
        onError?: (
            /** The mouse event that was the cause of the error. */
            event: BaseMouseEvent, args: BaseChartErrorEventArguments) => void;
        /** Function called when object pointer is on changes. */
        onHoverChange?: (
            /** The mouse event. */
            event: BaseMouseEvent, args: TArguments) => void;
        /** Function called whenever chart placement on screen changes. Note that this is called on every animation frame and is intended
        for painting overlays only. */
        onPositionChange?: (
            /** The mouse event that caused the event (if any) */
            event: BaseMouseEvent, args: TArguments) => void;
        /** Function called when user right clicks on chart. */
        onRightClick?: (
            /** The mouse event. */
            event: BaseMouseEvent, args: TClickArguments) => void;
        /** Function called when selected slices has changed. */
        onSelectionChange?: (
            /** The mouse event. */
            event: BaseMouseEvent, args: TArguments) => void;
        /** Function called when settings are changed. */
        onSettingsChange?: (
            /** An empty mouse event. */
            event: BaseMouseEvent, args: BaseChartSettingsChangeEventArguments) => void;
        /** Function called when user triple clicks on chart. Use it for custom function call. */
        onTripleClick?: (
            /** The mouse event. */
            event: BaseMouseEvent, args: TClickArguments) => void;
    }
    export interface BaseSettingsInteraction {
        /** Controls chart resize handles.
        It's a horizontal line right below any chart to handle whole chart resizes by small steps.
        
        Note that the full screen button used as a main alternative to get a full screen by one click. */
        resizing?: BaseSettingsResizer;
    }
    export interface BaseSettingsLabelStyle {
        /** Text alignment. */
        align?: string;
        /** The angle at which the label are rotated */
        angle?: number;
        /** Ratio between label sizes in different dimensions */
        aspectRatio?: number;
        /** Background style including fill color. */
        backgroundStyle?: BaseSettingsBackgroundStyle;
        /** Radius of item border. Similar to CSS border radius property. Zero radius will show a rectangle */
        borderRadius?: number;
        /** Label image. */
        image?: string;
        /** Slicing to use tiled images. Array of 4 values: [left, top, width, height] */
        imageSlicing?: [number, number, number, number];
        /** Vertical space between the lines of text. In multiples of text line height. */
        lineSpacing?: number;
        /** Margin around label text */
        margin?: number;
        /** Maximum width of the label. */
        maxWidth?: number;
        /** Padding between item content and item border. */
        padding?: number;
        /** Label text. */
        text?: string;
        /** Text style including fill color and font. */
        textStyle?: BaseSettingsTextStyle;
    }
    export interface BaseSettingsLegend {
        /** Settings to configure the legend marker appearance if disabled series corresponded. */
        advanced?: BaseSettingsLegendAdvanced;
        /** Show/hide chart legend. */
        enabled?: boolean;
        /** Whether to order entries to get possibly equal number of items into columns or rows. If false, once the row or column is full of entries,
        the next element will be first in the new row or column/rows. */
        equalizeRowsColumns?: boolean;
        /** Maximum height of the legend. If null, all available vertical space of chart will be consumed to set as much entries as possible.
        It coincides with the chart height if legend panel side is on a left or right. */
        height?: number;
        /** Vertical space between the lines of text. */
        lineSpacing?: number;
        /** Margin around each legend entry. */
        margin?: number;
        /** Visual element of legend entry with appropriate style to a slice color it corresponds. The content of each legend marker is the
        same as info popup appearing while hovering on slice. */
        marker?: BaseSettingsLegendMarker;
        /** Max number of symbols used in one line of text that applies to any legend entry. */
        maxLineSymbols?: number;
        /** Max number of columns. Use in conjunction with side parameter under the legend panel should be right or left in order to arrange entries by columns. */
        numberOfColumns?: number;
        /** Max number of rows. Use in conjunction with side parameter under the legend panel that should be set as bottom or top in order to arrange entries by rows. */
        numberOfRows?: number;
        /** Padding around each entry text and marker. */
        padding?: number;
        /** Legend enclosing panel settings. */
        panel?: BaseSettingsLegendPanel;
        /** Text settings displaying in legend entries. */
        text?: {
            fillColor?: string;
            font?: string;
        };
        /** Maximum width of the legend. If null, all available horizontal space of chart will be consumed to set as much entries as possible.
        It coincides with the chart width if legend panel side is on a top or bottom. */
        width?: number;
    }
    export interface BaseSettingsLegendAdvanced {
        /** Background color for selected legend entry. */
        selectedBackground?: string;
        /** Border color for selected legend entry. */
        selectedBorder?: string;
    }
    export interface BaseSettingsLegendMarker {
        /** Marker position relative to text */
        alignment?: string;
        /** Line color around marker shape */
        lineColor?: string;
        /** Marker size. */
        size?: number;
    }
    export interface BaseSettingsLegendPanel extends BaseSettingsChartPanel {
        padding?: number;
    }
    export interface BaseSettingsLineStyle {
        lineColor?: string;
        lineDash?: Array<number>;
        lineWidth?: number;
    }
    export interface BaseSettingsLocalization {
        /** Text used on menu close button. */
        closeButton?: string;
        /** Error message when data request has failed. */
        dataRequestFailed?: string;
        /** Message to show when data loading is in progress. */
        loadingLabel?: string;
        /** Strings used in toolbars. */
        toolbar?: BaseSettingsLocalizationToolbar;
    }
    export interface BaseSettingsLocalizationToolbar {
        backButton?: string;
        backTitle?: string;
        exportButton?: string;
        exportCSV?: string;
        exportJpeg?: string;
        exportPDF?: string;
        exportPNG?: string;
        exportTitle?: string;
        exportXLS?: string;
        fullscreenButton?: string;
        fullscreenTitle?: string;
        zoomoutButton?: string;
        zoomoutTitle?: string;
    }
    export interface BaseSettingsResizer {
        /** Enable/disable chart resizing. */
        enabled?: boolean;
        /** Whether to fix chart aspect ratio while resizing. */
        fixedAspect?: boolean;
        /** Distance from chart edge that will be used for resizing. */
        grabDistance?: number;
        /** Max pointer distance from chart edge when resize handle appears. */
        visibilityDistance?: number;
    }
    export interface BaseSettingsTextStyle {
        fillColor?: string;
        font?: string;
        shadowBlur?: number;
        shadowColor?: string;
        shadowOffsetX?: number;
        shadowOffsetY?: number;
    }
    export interface BaseSettingsTitle {
        /** Alignment of the title text. */
        align?: string;
        /** Show/hide chart title */
        enabled?: boolean;
        /** Whether to display title on the exported image. Note that it does not affect chart. */
        enabledOnExport?: boolean;
        /** Margin around title text, in px. */
        margin?: number;
        /** Title style */
        style?: {
            fillColor?: string;
            font?: string;
        };
        /** Title text. */
        text?: string;
    }
    export interface BaseSettingsToolbar {
        /** Toolbar align. Note that it can be overridden for individual items using item.align. Also Use 'top' or 'bottom' 
        sides in conjunction with 'left', 'right' align or use 'left', 'right' sides with 'top', 'bottom'. */
        align?: string;
        /** Whether to show back button in toolbar. */
        back?: boolean;
        /** CSS class name for the toolbar HTML panel. */
        cssClass?: string;
        /** Show/hide toolbar. */
        enabled?: boolean;
        /** Whether to show export dropdown in toolbar. */
        export?: boolean;
        /** A list of user defined items to show in toolbar. */
        extraItems?: Array<(string|BaseSettingsToolbarItem)>;
        /** A list of toolbar items. Use it to completely override the items in toolbar. */
        items?: Array<(string|BaseSettingsToolbarItem)>;
        /** Toolbar location inside chart. */
        location?: string;
        /** Whether to show or hide labels next to toolbar items by default. Note that it can be overridden for individual items using item.showLabels. */
        showLabels?: boolean;
        /** Toolbar placement side. Note that it can be overridden for individual items using item.side. */
        side?: string;
        /** Whether to show the zoom out button. */
        zoomOut?: boolean;
    }
    export interface BaseSettingsToolbarItem {
        /** Item align. */
        align?: string;
        /** CSS class name. */
        cssClass?: string;
        /** Item image, URL to image. */
        image?: string;
        /** Built in toolbar item name. */
        item?: string;
        /** Item label */
        label?: string;
        /** Item location */
        location?: string;
        /** Function to execute on item click. */
        onClick?: (event: MouseEvent, chart: BaseApi) => void;
        /** Whether to show button label. */
        showLabel?: boolean;
        /** Item side. */
        side?: string;
        /** Item title, shown on hover. */
        title?: string;
    }
    export interface FacetChartFacet {
        activeItemId: string;
        count: number;
        data: PieChartPieData;
        from: number;
        getActiveItem(): FacetChartItem;
        id: string;
        items: Array<FacetChartItem>;
        left: number;
        loading: boolean;
        offset: number;
        opacity: number;
        parentItem: FacetChartItem;
        rebuild: boolean;
        right: number;
        to: number;
        totalCount: number;
    }
    /** Describes the base properties shared between all events raised by the different charts. */
    export interface FacetChartChartClickEventArguments extends FacetChartChartEventArguments {
        clickFacet: FacetChartFacet;
        clickItem: FacetChartItem;
    }
    /** Describes the base properties shared between all events raised by the different charts. */
    export interface FacetChartChartEventArguments extends BaseChartEventArguments {
        count: number;
        facet: FacetChartFacet;
        hoverItem: FacetChartItem;
        offset: number;
        selection: Array<FacetChartItem>;
    }
    export interface FacetChartItem {
        active: boolean;
        currentLabel: BaseLabel;
        data: PieChartDataObject;
        expandable: boolean;
        facet: FacetChartFacet;
        id: string;
        index: number;
        innerFacet: FacetChartFacet;
        label: string;
        selected: boolean;
        url: string;
        values: Array<FacetChartItemValue>;
    }
    export interface FacetChartItemValue {
        currentLabel: BaseLabel;
        data: PieChartDataObject;
        facet: FacetChartFacet;
        id: string;
        index: number;
        label: string;
        /** data to preview if previewContents = true */
        previewData: Array<number>;
        seriesId: string;
        style: FacetChartSettingsFacetStyle;
        value: number;
    }
    export interface FacetChartSettings extends LinearChartSettings {
        /** Default series settings for each series rendering type. Use this to configure all series of specific type to get line
        or column chart or combination of them. */
        chartTypes?: {
            /** Series type to render values as vertical bars. */
            columns?: FacetChartSettingsSeriesColumns;
            /** Series type to connect value points by lines. */
            line?: FacetChartSettingsSeriesLines;
        };
        /** Settings used to load data into chart. Customise preferred data source feeding methods.
        You can use one of these options: url, data function, preloaded data. */
        data?: Array<PieChartSettingsData>;
        /** The events used to handle user interaction with UI elements. */
        events?: LinearChartSettingsEvents<FacetChartChartEventArguments, FacetChartChartClickEventArguments>;
        /** Chart x-axis line rendered at the bottom horizontally to display group names under each bar. */
        facetAxis?: FacetChartSettingsFacetAxis;
        /** Configurable conditions to filter the raw data values for subset of drawing facets. */
        filters?: {
            /** Determine whether to show item or not. */
            sliceFilter?: (
                /** the item to test */
                item: PieChartDataObject) => boolean;
        };
        /** A variety of interaction options that includes scrolling, zooming and swipe. */
        interaction?: FacetChartSettingsInteraction;
        /** Series cluster including name placed on facet axis. */
        items?: {
            /** Facet item style. */
            style?: {
                /** Whether to open series item on click */
                expandable?: boolean;
                /** Facet item text */
                label?: string;
            };
            /** Dynamically determine item style from data. */
            styleFunction?: (
                /** item to apply predefined style */
                item: FacetChartItem, 
                /** data relevant to item */
                itemData: PieChartDataObject) => void;
        };
        /** The chart legend by additional interactivity to change the visibility of series it corresponds. */
        legend?: LinearChartSettingsLegend;
        /** Settings to specify initial view once the page loaded. */
        navigation?: {
            /** Initial facet drilldown to show. For example ['', 'Firefox', 'Firefox 2.5'] denotes to various browsers grouped by versions. */
            initialDrilldown?: Array<string>;
            /** Initial scroll offset, number of items from start. */
            initialOffset?: number;
        };
        /** Left, right scroll buttons if additional facets available */
        scrollButtons?: FacetChartSettingsScrollButtons;
        /** Array of series in the chart. Each of the series can be different type, can use different data source and
        aggregation. Additionally, series can be clustered and stacked. */
        series?: Array<FacetChartSettingsSeries>;
        /** The default series used as the chart dominant data. Use settings.series array to specify actual series. */
        seriesDefault?: FacetChartSettingsSeries;
        /** Theme to apply. You can either use this to share configuration objects between multiple charts or use one of the predefined
        themes. */
        theme?: FacetChartSettings;
        /** Adjustable settings to manage default and custom toolbar items, as well as toolbar overall appearance. */
        toolbar?: FacetChartSettingsToolbar;
    }
    export interface FacetChartSettingsFacetAxis {
        /** Default width of one item. Used to calculate initial view. */
        defaultUnitWidth?: number;
        /** Show/hide facet axis. */
        enabled?: boolean;
        /** Facet axis name settings. */
        labels?: FacetChartSettingsFacetAxisLabels;
        /** Maximum width of one item. */
        maxUnitWidth?: number;
        /** Height of the x axis. */
        size?: number;
    }
    export interface FacetChartSettingsFacetAxisLabels extends BaseSettingsLabelStyle {
        enabled?: boolean;
        interLabelSpacing?: number;
    }
    export interface FacetChartSettingsFacetStyle extends FacetChartSettingsSeriesColumnsStyle {
    }
    export interface FacetChartSettingsInteraction extends LinearChartSettingsInteraction {
        /** Chart animation settings. */
        animation?: {
            /** Duration of scroll animation. */
            scrollDuration?: number;
        };
        /** Configurable settings for select option. */
        selection?: FacetChartSettingsInteractionSelection;
    }
    export interface FacetChartSettingsInteractionSelection {
        /** Enable/disable selection */
        enabled?: boolean;
        tolerance?: number;
    }
    export interface FacetChartSettingsScrollButtons {
        /** Show/hide left, right scroll buttons if additional facets available. */
        enabled?: boolean;
        /** Whether to display title on the exported image. Note that it does not affect chart. */
        enabledOnExport?: boolean;
        /** Button height or width. */
        size?: number;
        /** Scroll button style */
        style?: {
            fillColor?: string;
            hoverFillColor?: string;
            hoverLineColor?: string;
            lineColor?: string;
        };
    }
    export interface FacetChartSettingsSeries extends LinearChartSettingsSeries {
        /** Data manipulation settings used for default series. */
        data?: FacetChartSettingsSeriesData;
        /** Series type. */
        type?: string;
    }
    export interface FacetChartSettingsSeriesColumns extends LinearChartSettingsSeriesColumns {
        /** Data manipulation settings used for default series. */
        data?: FacetChartSettingsSeriesData;
        /** Default column style. */
        style?: FacetChartSettingsSeriesColumnsStyle;
    }
    export interface FacetChartSettingsSeriesColumnsStyle extends LinearChartSettingsSeriesColumnsStyle {
        /** Set to `true` in order to display a "preview" of the value distribution in each column.
        The line color is specified in `previewLineColor` */
        previewContents?: boolean;
        /** The color of the line used to draw the distribution if `previewContents` is `true`. */
        previewLineColor?: string;
    }
    export interface FacetChartSettingsSeriesData extends LinearChartSettingsSeriesData {
        /** Object property of subvalue that will be used for series data aggregation. An alternative is to use `valueFunction`. If neither are specified
        the field `value` is implied. */
        field?: string;
    }
    export interface FacetChartSettingsSeriesLines extends LinearChartSettingsSeriesLines {
        /** Data manipulation settings used for default series. */
        data?: FacetChartSettingsSeriesData;
    }
    export interface FacetChartSettingsToolbar extends BaseSettingsToolbar {
        /** Show/hide toolbar. */
        enabled?: boolean;
        /** A list of toolbar items. Use it to completely override the items in toolbar. */
        items?: Array<BaseSettingsToolbarItem>;
        /** Whether to show the Lin/Log button in the toolbar. */
        logScale?: boolean;
        /** Whether to show the zoom out button. */
        zoomOut?: boolean;
    }
    export interface GeoChartAggregationDataObjectNode extends GeoChartGeoDataObjectNode {
        /** Contains all original node data objects that have been aggregated into this instance */
        aggregatedNodes: Array<GeoChartGeoDataObjectNode>;
        aggregatedWeight: number;
    }
    export interface GeoChartDataObject extends BaseDataErrorResponse {
        east: number;
        links: Array<GeoChartGeoDataObjectLink>;
        nodes: Array<GeoChartGeoDataObjectNode>;
        north: number;
        south: number;
        west: number;
    }
    export interface GeoChartDataRequest {
        aggregateLat: Array<number>;
        aggregateLng: Array<number>;
        aggregateMinItems: number;
        east: number;
        id: string;
        layerId: string;
        north: number;
        south: number;
        west: number;
        zoom: number;
    }
    export interface GeoChartGeoDataObjectLink extends ItemsChartDataObjectLink {
    }
    export interface GeoChartGeoDataObjectNode extends ItemsChartDataObjectNode {
        aggregate?: boolean;
        /** Coordinates can specify a point (for example, [0, 2]) or a shape (an array containing separate polygons, where each polygon
        is an array of concatenated X/Y coordinate pairs, for example [[0,2,2,2,2,0],[10,10,12,12]]) */
        coordinates?: ([number, number]|Array<Array<number>>);
        count?: number;
        /** If the data object has been generated from GeoJSON, this property will contain the entire feature object from which this data object was created. */
        shapeFeature?: any;
        /** If the data object has been generated from GeoJSON, this property will contain the entire geometry object from which this data object was created. */
        shapeGeometry?: any;
        type?: string;
    }
    export interface GeoChartSettings extends ItemsChartSettings {
        /** Chart area related settings. */
        area?: GeoChartSettingsArea;
        background?: GeoChartSettingsBackground;
        /** Settings used to load data into chart. Customise preferred data source feeding methods.
        You can use one of these options: url, data function, preloaded data. */
        data?: Array<GeoChartSettingsData>;
        filters?: {
            nodeFilter?: (node: GeoChartGeoDataObjectNode) => boolean;
        };
        /** Customise chart resize handles or animation duration settings. */
        interaction?: GeoChartSettingsInteraction;
        /** The default values for specific layer types. Any custom values in specified in `layers` property will be applied on top of the
        values specified here, based on the `type` property witin them. */
        layerTypes?: {
            aggregateOnShapes?: GeoChartSettingsLayerAggregated;
            charts?: GeoChartSettingsLayerCharts;
            items?: GeoChartSettingsLayerItems;
            shapes?: GeoChartSettingsLayerShapes;
        };
        /** An array of objects. Have a look at "layerTypes" for the possible object types */
        layers?: Array<GeoChartSettingsLayerBase>;
        /** Specifies the defaults for all layers, independent of the layer type.
        @deprecated This section is no longer used, specify the defaults in `layerTypes` section, based on the appropriate layer type. */
        layersDefault?: GeoChartSettingsLayerBase;
        navigation?: {
            drilldownLayer?: string;
            initialDrilldown?: Array<string>;
            /** Specify latitude of the initial center coordinate (WGS-84 degrees) */
            initialLat?: number;
            /** Specify longitude of the initial center coordinate (WGS-84 degrees) */
            initialLng?: number;
            /** Specify the initial zoom level for the chart. (For most chart data sources, valid values are 0..18 inclusive). */
            initialZoom?: number;
            maxBounds?: {
                east?: number;
                north?: number;
                south?: number;
                west?: number;
            };
            maxZoom?: number;
            minZoom?: number;
        };
        style?: {
            fadeTime?: number;
            selection?: {
                fillColor?: string;
                sizeConstant?: number;
                sizeProportional?: number;
            };
        };
        /** Theme to apply. You can either use this to share configuration objects between multiple charts or use one of the predefined
        themes. */
        theme?: GeoChartSettings;
        /** Adjustable settings to manage default and custom toolbar items, as well as toolbar overall appearance. */
        toolbar?: BaseSettingsToolbar;
    }
    export interface GeoChartSettingsAggregation {
        /** The approximate minimum distance in pixels between two nodes before they are aggregated together. */
        distance?: number;
        /** Enables/Disables automatic aggregation */
        enabled?: boolean;
        /** The last zoom level on which the nodes are automatically agregated. When the chart is zoomed in deeper, the original
        nodes will be used instead. */
        maxZoom?: number;
        /** A delegate that can be used to process aggregated nodes before links are aggregated.
        The delegate should modify the given array and mapping information if some nodes should be
        aggregated differently. */
        postProcessAggregatedNodes?: (
            /** The aggregated node objects. */
            nodes: Array<GeoChartAggregationDataObjectNode>, 
            /** A dictionary that maps the original node ID to the aggregated node (the same instance that is present in the `nodes` array). */
            map: Dictionary<GeoChartAggregationDataObjectNode>, 
            /** An empty array that the delegate is expected to fill with aggregated nodes that have been modified. */
            modifiedNodes: Array<GeoChartAggregationDataObjectNode>) => void;
        /** Function that returns the value used as weight in the aggregation process for each node. */
        weightFunction?: (node: GeoChartGeoDataObjectNode) => number;
    }
    export interface GeoChartSettingsArea extends BaseSettingsArea {
        /** Area style. */
        style?: GeoChartSettingsAreaStyle;
    }
    export interface GeoChartSettingsAreaStyle extends BaseSettingsAreaStyle {
    }
    export interface GeoChartSettingsBackground {
        enabled?: boolean;
        params?: {
            attribution?: string;
            subdomains?: Array<string>;
        };
        type?: string;
        url?: string;
    }
    export interface GeoChartSettingsData extends ItemsChartSettingsData {
        aggregationGridSize?: number;
        aggregationMinCount?: number;
        bounds?: [number, number, number, number];
        dataFunction?: (request: GeoChartDataRequest, success: (data: GeoChartDataObject) => void, fail: (result: BaseDataErrorResponse) => void) => void;
        /** Data loading format. Currently only JSON supported. */
        format?: string;
        maxRequestRect?: [number, number];
        /** If true, data source will use bounds to limit response, if false, assume all data is returned. */
        perBoundsData?: boolean;
        /** If true, separate data aviable at each drilldown level. Used in Renderer. */
        perDrilldownData?: boolean;
        /** If true, separate data available at each zoom level. Used in Renderer and here. */
        perZoomData?: boolean;
        prefetchRatio?: number;
        /** Provides the ability to embed chart data directly into the chart configuration.
        Data can be represented by an JavaScript object or a JSON string. */
        preloaded?: (string|GeoChartDataObject);
        wrapLng?: boolean;
    }
    export interface GeoChartSettingsInteraction extends ItemsChartSettingsInteraction {
        mode?: string;
    }
    export interface GeoChartSettingsLayerAggregated extends GeoChartSettingsLayerOverlay {
        /** Aggregation function to use. */
        aggregation?: string;
        /** Data field used for aggregation. */
        aggregationField?: string;
        /** Custom function to call for aggregation */
        aggregationFunction?: (
            /** array of nodes matching single shape */
            nodesArray: Array<GeoChartGeoDataObjectNode>) => number;
        /** The ID of a linked shapes layer. Style changes will be applied to this layer. The shapes layer must be defined before the aggregation layer. */
        shapesLayer?: string;
        /** Function for applying the aggregated value on node. */
        styleFunction?: (
            /** shape node style is being applied to */
            node: ItemsChartNode, 
            /** computed aggregate value */
            value: number) => void;
    }
    export interface GeoChartSettingsLayerBase {
        data?: {
            id?: string;
        };
        enabled?: boolean;
        id?: string;
        maxZoom?: number;
        minZoom?: number;
        name?: string;
        /** Forces style re-evaluation on zoom change. Use to provide zoom dependant style. */
        perZoomStyle?: boolean;
        type?: string;
    }
    export interface GeoChartSettingsLayerCharts extends GeoChartSettingsLayerOverlay {
        /** Chart type to use */
        chartType?: string;
        data?: {
            /** ID should be `null` because the charts layer does not retrieve data this way. */
            id?: string;
        };
        /** The settings that will be passed to each nested chart. Note that if `settingsFunction` is also specified, these
        settings will not be used and `settingsFunction` will be called instead. */
        settings?: (PieChartSettings|TimeChartSettings|FacetChartSettings);
        /** The delegate that will be used to retrieve custom setting for a particular node. This function is called every time the node is modified.
        The result should only contain modified settings that will be passed to `chart.updateSettings()`. */
        settingsFunction?: (node: ItemsChartNode, data: GeoChartGeoDataObjectNode) => (PieChartSettings|TimeChartSettings|FacetChartSettings);
        /** The charts layer will take data from shapes or nodes layer and creates a chart for every shape/node. */
        shapesLayer?: string;
    }
    export interface GeoChartSettingsLayerItems extends GeoChartSettingsLayerBase {
        /** Controls automatic proximity based aggregation. */
        aggregation?: GeoChartSettingsAggregation;
        layout?: GeoChartSettingsNodesLayout;
        style?: GeoChartSettingsNodesLayerStyle;
    }
    export interface GeoChartSettingsLayerOverlay extends GeoChartSettingsLayerBase {
        /** The ID of a linked shapes layer. The shapes layer must be defined before the overlay layer. */
        shapesLayer?: string;
    }
    export interface GeoChartSettingsLayerShapes extends GeoChartSettingsLayerBase {
        style?: GeoChartSettingsShapesLayerStyle;
    }
    export interface GeoChartSettingsNodesLayerStyle extends ItemsChartSettingsNodesLayerStyle {
        /** The style used to draw convex shapes for the automatically aggregated nodes. */
        aggregatedShape?: BaseSettingsBackgroundStyle;
    }
    export interface GeoChartSettingsNodesLayout {
        /** Anchor strength, relative to link strength. */
        anchorStrength?: number;
        /** Layout mode. */
        mode?: string;
        /** Desired distance between nodes. */
        nodeSpacing?: number;
    }
    export interface GeoChartSettingsShapesLayerNodeStyle extends ItemsChartSettingsNodeStyle {
        /** Specifies if the node representing the shape can be drilled down into. */
        expandable?: boolean;
    }
    export interface GeoChartSettingsShapesLayerStyle extends GeoChartSettingsNodesLayerStyle {
        /** Default node style. */
        node?: GeoChartSettingsShapesLayerNodeStyle;
        /** Controls shape simplification to improve rendering performance.
        Distance in pixels between points to simplify.
        Use 0 to disable simplification. */
        shapeSimplificationPrecision?: number;
    }
    /** Describes the base properties shared between all events raised by the different charts. */
    export interface ItemsChartChartClickEventArguments extends ItemsChartChartEventArguments {
        clickItem: BaseLabel;
        clickLink: ItemsChartLink;
        clickNode: ItemsChartNode;
    }
    /** Describes the base properties shared between all events raised by the different charts. */
    export interface ItemsChartChartEventArguments extends BaseChartEventArguments {
        chartX: number;
        chartY: number;
        hoverItem: (ItemsChartItemsLayerLinkItem|ItemsChartItemsLayerNodeItem);
        hoverLink: ItemsChartLink;
        hoverNode: ItemsChartNode;
        selection: Array<(ItemsChartLink|ItemsChartNode)>;
    }
    export interface ItemsChartDataObjectBase extends BaseDataErrorResponse {
    }
    export interface ItemsChartDataObjectLink extends ItemsChartDataObjectBase {
        className?: string;
        /** The ID of the node where the link originates. */
        from: string;
        /** The unique identifier of the link. */
        id?: string;
        style?: ItemsChartSettingsLinkStyle;
        /** The ID of the target node. */
        to: string;
        value?: number;
    }
    export interface ItemsChartDataObjectNode extends ItemsChartDataObjectBase {
        className?: string;
        /** The unique identifier of the node. */
        id: string;
        loaded: boolean;
        style?: ItemsChartSettingsNodeStyle;
    }
    export interface ItemsChartLink extends ItemsChartSettingsLinkStyle {
        /** Data object that this link represents. */
        data: ItemsChartDataObjectLink;
        /** Node at the start of the link. */
        from: ItemsChartNode;
        /** Whether or not the mouse cursor is hovering over the link. */
        hovered: boolean;
        /** ID of the link */
        id: string;
        /** Specifies if the link is invisible - thus completely skipping the drawing and hit testing. However the link is not removed.
        This flag is set only by user code. */
        invisible: boolean;
        /** Link relevance is used in NetChart when the navigation mode is `focusnodes` and is intended to be a rough measure of how "interesting" a link is.
        For information about what relevance is and how it's calculated, see the [Focusnodes algorithm](net-chart/advanced-topics/focusnodes-algorithm-details.html). */
        relevance: number;
        /** If this is `false`, then the node is visible. If it is `true` or a non-zero a number (a "truthy" value), then the removal animation is in progress.
        After the removal animation completes, the node will be hidden. */
        removed: (boolean|number);
        /** Whether or not the link is selected. */
        selected: boolean;
        /** Node at the end of the link. */
        to: ItemsChartNode;
    }
    /** A visible node. */
    export interface ItemsChartNode extends ItemsChartSettingsNodeStyle {
        /** Whether or not the node is a background node. This flag is set only by user code. */
        background: boolean;
        /** Data object that this node represents */
        data: ItemsChartDataObjectNode;
        /** Data obects for all links that are attached to the node. Note that not all of them may be visible. Only links with both end nodes visible are shown. */
        dataLinks: Array<ItemsChartDataObjectLink>;
        /** Whether or not the node is expanded. A node counts as "expanded" if all its links are visible. */
        expanded: boolean;
        focused: boolean;
        /** Whether or not the mouse cursor is hovering over the node. */
        hovered: boolean;
        /** ID of the node */
        id: string;
        /** Specifies if the node is invisible - thus completely skipping the drawing and hit testing. However the node is not removed.
        This flag is set only by user code. */
        invisible: boolean;
        /** A list of additional items for the particular node. To add or remove items, change this array. */
        items: Array<ItemsChartSettingsNodeItem>;
        /** Visible links attached to the node. */
        links: Array<ItemsChartLink>;
        /** Node relevance is used in NetChart when the navigation mode is `focusnodes` and is intended to be a rough measure of how "interesting" a node is.
        For information about what relevance is and how it's calculated, see the [Focusnodes algorithm](net-chart/advanced-topics/focusnodes-algorithm-details.html). */
        relevance: number;
        /** If this is `false`, then the node is visible. If it is `true` or a non-zero a number (a "truthy" value), then the removal animation is in progress.
        After the removal animation completes, the node will be hidden. */
        removed: (boolean|number);
        /** Whether or not the node is selected. */
        selected: boolean;
        /** If true, the node is fixed in place and does not get affected by layout algorithms. This gets set automatically after the user drags a node in NetChart. */
        userLock: boolean;
    }
    export interface ItemsChartItemsLayerLinkItem extends ItemsChartItemsLayerLinkLabel {
        /** Offset from link center along link direction. In pixels. */
        lx?: number;
        /** Offset from link center perpendicular to link direction. In pixels. */
        ly?: number;
        /** Offset from link center along link direction. A fraction of link length. */
        px?: number;
        /** Offset from link center perpendicularly to link direction. A fraction of link radius. */
        py?: number;
    }
    export interface ItemsChartItemsLayerLinkLabel extends ItemsChartItemsLayerNodeLabel {
        /** Whether to rotate link labels in the same direction as link */
        rotateWithLink?: boolean;
    }
    export interface ItemsChartItemsLayerNodeItem extends ItemsChartItemsLayerNodeLabel {
        /** X offset from node center. A fraction of node width. */
        px?: number;
        /** Y offset from node center. A fraction of node height. */
        py?: number;
    }
    export interface ItemsChartItemsLayerNodeLabel extends BaseLabel {
        /** Whether to apply different scale according to initial size of the link or node. */
        scaleWithSize?: boolean;
        /** Whether to apply the scale if zoom changes. If false, the label size never changes. */
        scaleWithZoom?: boolean;
    }
    export enum ItemsChartNodeAnchorMode {
        Fixed = 2,
        /** Node if free-floating, */
        Floating = 0,
        Scene = 1,
    }
    export interface ItemsChartSettings extends BaseSettings {
        advanced?: ItemsChartSettingsAdvanced;
        /** Settings used to load data into chart. Customise preferred data source feeding methods.
        You can use one of these options: url, data function, preloaded data. */
        data?: Array<ItemsChartSettingsData>;
        /** The events used to handle user interaction with UI elements. */
        events?: BaseSettingsEvents<ItemsChartChartEventArguments, ItemsChartChartClickEventArguments>;
        /** Info popup for item - meaning links or nodes - with configurable content rendered. */
        info?: {
            /** Show/hide info popup */
            enabled?: boolean;
            /** Returns html string to display in passed links info popup. */
            linkContentsFunction?: (
                /** link data */
                linkData: ItemsChartDataObjectLink, 
                /** link object */
                link: ItemsChartLink, 
                /** callback function */
                asyncCallback: (contents: string) => void) => string;
            /** Returns html string to display in passed nodes info popup. */
            nodeContentsFunction?: (
                /** node data */
                nodeData: ItemsChartDataObjectNode, 
                /** node object */
                node: ItemsChartNode, 
                /** callback function */
                callback: (contents: string) => void) => string;
        };
        /** Customise chart resize handles or animation duration settings. */
        interaction?: ItemsChartSettingsInteraction;
        /** Configurable link menu with option to specify a range of displaying buttons. */
        linkMenu?: ItemsChartSettingsLinkMenu;
        /** Configurable node menu with option to specify a range of displaying buttons. */
        nodeMenu?: ItemsChartSettingsNodeMenu;
        style?: {
        };
    }
    export interface ItemsChartSettingsAdvanced extends BaseSettingsAdvanced {
        /** Whether to display a loading indicator on every node, otherwise only a global loading indicator. */
        perNodeLoadingIndicator?: boolean;
    }
    /** If display attribute is "customShape", SettingsCustomShape are
    required to determine how to render the custom shape */
    export interface ItemsChartSettingsCustomShape {
        /** When drawing links/arrows to the shape it may be desirable to determine the point where the arrow
        reaches and touches the respective shape. Given the start point of the ray, this function should return
        the distance to the outer border of the shape */
        distanceToEdge?: (
            /** radius */
            r: number, 
            /** width */
            w: number, 
            /** the ray towards shape */
            ddx: number, 
            /** the ray towards shape */
            ddy: number) => number;
        /** Function to test if hover action was over the respective shape. The function will be called in the context of the appropriate INode (i.e. "this"=INode). */
        hitTest?: (
            /** X coordinate of the hit test */
            x: number, 
            /** Y coordinate of the hit test */
            y: number, 
            /** Node scalign factor */
            itemScale: number, 
            /** Previous closest distance. */
            prevBest: number) => number;
        /** Method performed on update (such as hover). The function will be called in the context of the appropriate INode (i.e. "this"=INode). */
        onUpdate?: (
            /** The canvas on which the node will be rendered */
            context: CanvasRenderingContext2D, 
            /** The node radius radius, also available as "this.radius" */
            radius: number) => {
                /** Bounds of the node for the given radius. The values are [x0, y0, x1, y1] for the upper left and lower right corner respectively. */
                bounds: [number, number, number, number];
                /** One half of the width of the node. */
                HWidth: number;
                /** One half of the height of the node */
                HHeight: number;
                /** Optional. The anchor point [x,y] where the links will be attached. If not specified, the anchor points will be left at defaults. */
                anchor?: [number, number];
            };
        /** Function to render the custom shape in canvas 2d context. The function will be called in the context of the appropriate INode (i.e. "this"=INode). */
        paint?: (
            /** The canvas 2d rendering context for rendering */
            context: CanvasRenderingContext2D, 
            /** The X value of the center coordinate, where node needs to be rendered */
            x: number, 
            /** The Y value of the center coordinate where the node needs to be rendered */
            y: number, 
            /** Half of the width of the node */
            hWidth: number, 
            /** Half of the height of the node */
            hHeight: number, 
            /** Image of the node, if any. */
            image: (HTMLImageElement|HTMLCanvasElement), 
            /** Whether or not to paint details (image, label). When the node is zoomed out far enough, the details aren't painted. */
            paintDetails: boolean) => void;
        /** Function to render the selection shape for custom shape in canvas 2d context.
        The function will be called in the context of the appropriate INode (i.e. "this"=INode). */
        paintSelection?: (
            /** The canvas 2d rendering context for rendering */
            context: CanvasRenderingContext2D, 
            /** The X value of the center coordinate, where node needs to be rendered */
            x: number, 
            /** The Y value of the center coordinate where the node needs to be rendered */
            y: number, 
            /** Half of the width of the node */
            hWidth: number, 
            /** Half of the height of the node */
            hHeight: number) => void;
    }
    export interface ItemsChartSettingsData extends BaseSettingsData {
        /** Count of caching items including only links and nodes. */
        cacheSize?: number;
        /** Specifies the random layout method: grid, tree, uniform. The default is uniform. */
        random?: string;
        randomGridLinkProbability?: number;
        /** Generates random data. */
        randomLinks?: number;
        /** Generates random data. Specify a value larger than 0 to enable generating random data. */
        randomNodes?: number;
        randomTreeDensity?: number;
        /** Max number of nodes to submit in a single request. */
        requestMaxUnits?: number;
    }
    export interface ItemsChartSettingsInteraction extends BaseSettingsInteraction {
        /** Whether to allow moving nodes around. */
        nodesMovable?: boolean;
        /** Move chart vertically and horizontally by clicking on the main chart pane and dragging it on any direction. */
        panning?: {
            /** Enables/disables chart panning. */
            enabled?: boolean;
        };
        /** Select node to expand it or getting path. */
        selection?: ItemsChartSettingsInteractionSelection;
        /** Zoom in or out by swiping up or down with mouse scroll pad or by using the Zoom-out feature at the top left. */
        zooming?: ItemsChartSettingsInteractionZooming;
    }
    export interface ItemsChartSettingsInteractionSelection {
        /** Whether to move nodes outside of screen area. */
        allowMoveNodesOffscreen?: boolean;
        /** Enable/disable selection. */
        enabled?: boolean;
        /** Whether to set links selectable. */
        linksSelectable?: boolean;
        /** Whether to retain nodes location after being moved by the user. */
        lockNodesOnMove?: boolean;
        /** Whether to set nodes selectable. */
        nodesSelectable?: boolean;
        /** Max click distance from object that still counts as click on the object. */
        tolerance?: number;
    }
    export interface ItemsChartSettingsInteractionZooming {
        /** Whether to auto zoom every time user clicks on the chart. */
        autoZoomAfterClick?: boolean;
        /** A double-click on empty space will trigger zoom in by this value. Set to null or false to disable double click zooming. */
        doubleClickZoom?: number;
        /** Whether to zoom by two finger pinch. */
        fingers?: boolean;
        /** Sensitivity of wheel zoom. */
        sensitivity?: number;
        /** Whether to zoom by mouse wheel. */
        wheel?: boolean;
        /** Enable/Disable zoom in on double click. */
        zoomInOnDoubleClick?: boolean;
    }
    export interface ItemsChartSettingsItemsLayerLabelStyle extends BaseSettingsLabelStyle {
        /** Whether to apply different scale according to initial size of the link or node. */
        scaleWithSize?: boolean;
        /** Whether to apply the scale if zoom changes. If false, the label size never changes. */
        scaleWithZoom?: boolean;
    }
    export interface ItemsChartSettingsItemsLayerLinkLabelStyle extends ItemsChartSettingsItemsLayerLabelStyle {
        /** Whether to rotate link labels in the same direction as link */
        rotateWithLink?: boolean;
    }
    export interface ItemsChartSettingsItemsLayerStyle {
        /** An "all included" style function. */
        allObjectsStyleFunction?: (
            /** all nodes that currently loaded on the chart */
            nodes: Array<ItemsChartNode>, 
            /** all links that are currently loaded on the chart */
            links: Array<ItemsChartLink>) => {
                modifiedNodes: Array<ItemsChartNode>;
                modifiedLinks: Array<ItemsChartLink>;
            };
        /** Items are small UI elements that provide extra information. Items are attached to nodes or links and can display a label, image or both. */
        item?: ItemsChartSettingsItemsLayerLabelStyle;
        /** Default link style. */
        link?: ItemsChartSettingsLinkStyle;
        /** The class rules to apply individual style for link subset. Use known CSS class definition practice.
        Define one or more classes by specifying name and style and apply those by node definition. If multiple
        classes applied separate them with space and class rules will cascade in order of class definition. */
        linkClasses?: Array<{
                /** Class name */
                className?: string;
                /** Style settings */
                style?: ItemsChartSettingsLinkStyle;
            }>;
        /** Link decorations are not painted if link is shorter than this. */
        linkDecorationMinSize?: number;
        /** Link decoration size calculated as a product of the link radius and scale factor. The value lower than linkDecorationMinSize will take no effect. */
        linkDecorationScale?: number;
        /** Link details like labels, items are hidden if link width in pixels is below this value. */
        linkDetailMinSize?: number;
        /** Link details like labels, items are not rendered if chart zoom is below this value. */
        linkDetailMinZoom?: number;
        /** Additional style to apply when a link is hovered. */
        linkHovered?: ItemsChartSettingsLinkStyle;
        /** Link text style */
        linkLabel?: ItemsChartSettingsItemsLayerLinkLabelStyle;
        /** Base node size of link label that scales with link size. */
        linkLabelScaleBase?: number;
        /** An object defining one or more functions used to calculate node rendering style.
        Structure: { 'rule1':function1(nodeObj), 'rule2':function2(nodeObj) }
        The functions are executed in lexicographic order whenever node data or links change.
        Each function can modify the nodeObj to add specific style elements.
        @deprecated use linkStyleFunction instead */
        linkRules?: Dictionary<(node: ItemsChartLink) => void>;
        /** Additional style to apply when link is selected. */
        linkSelected?: ItemsChartSettingsLinkStyle;
        /** A a style function for links. Will be called whenever a link property or data has changed.
        Use to dynamically set link style fields. */
        linkStyleFunction?: (node: ItemsChartLink) => void;
        /** Default node style. */
        node?: ItemsChartSettingsNodeStyle;
        /** Default node  anchor style. */
        nodeAnchor?: ItemsChartSettingsNodeAnchorStyle;
        /** Additional style to apply for background nodes. */
        nodeBackground?: ItemsChartSettingsNodeStyle;
        /** The class rules to apply individual style for node subset. Use known CSS class definition practice.
        Define one or more classes by specifying name and style and apply those by node definition. If multiple
        classes applied separate them with space and class rules will cascade in order of class definition. */
        nodeClasses?: Array<{
                /** Class name */
                className?: string;
                /** Style settings */
                style?: ItemsChartSettingsNodeStyle;
            }>;
        /** Node details like labels, items, images are hidden if node width in pixels is below this value. */
        nodeDetailMinSize?: number;
        /** Node details like labels, items, images are not rendered if chart zoom is below this value. */
        nodeDetailMinZoom?: number;
        /** Additional style to apply when node is expanded. */
        nodeExpanded?: ItemsChartSettingsNodeStyle;
        /** Additional style to apply when node is focused. */
        nodeFocused?: ItemsChartSettingsNodeStyle;
        /** Additional style to apply when a node is hovered. */
        nodeHovered?: ItemsChartSettingsNodeStyle;
        /** Node label style. */
        nodeLabel?: ItemsChartSettingsItemsLayerLabelStyle;
        /** Base node size of node label that scales with node size. */
        nodeLabelScaleBase?: number;
        /** Additional style to apply when a node position is locked. */
        nodeLocked?: ItemsChartSettingsNodeStyle;
        /** Additional style to apply when node data is not yet loaded. */
        nodeNotLoaded?: ItemsChartSettingsNodeStyle;
        /** An object defining one or more functions used to calculate node rendering style.
        @deprecated use nodeStyleFunction instead */
        nodeRules?: Dictionary<(node: ItemsChartNode) => void>;
        /** Additional style to apply when node is selected. */
        nodeSelected?: ItemsChartSettingsNodeStyle;
        /** A a style function for nodes. Will be called whenever a node property or data has changed.
        Use to dynamically set node style fields. */
        nodeStyleFunction?: (node: ItemsChartNode) => void;
        /** Color for fade out animation of removed objects. */
        removedColor?: string;
        /** Determines if link radius (width) is automatically scaled when the chart is zoomed in or out.
        If set to `null`, the value is inherited from `scaleObjectsWithZoom` */
        scaleLinksWithZoom?: boolean;
        /** Determines if node radius is automatically scaled when the chart is zoomed in or out.
        If `scaleLinksWithZoom` is not set, this value also impacts links. */
        scaleObjectsWithZoom?: boolean;
        selection?: {
            fillColor?: string;
            lineColor?: string;
            shadowBlur?: number;
            shadowColor?: string;
            shadowOffsetX?: number;
            shadowOffsetY?: number;
            sizeConstant?: number;
            sizeProportional?: number;
        };
    }
    /** Link item. */
    export interface ItemsChartSettingsLinkItem extends ItemsChartSettingsItemsLayerLabelStyle {
        /** Offset from link center along link direction. In pixels. */
        lx?: number;
        /** Offset from link center perpendicular to link direction. In pixels. */
        ly?: number;
        /** Offset from link center along link direction. A fraction of link length.
        Value of -1 places the item at the start of the link.
        Value of 1 places the item at the end of the link. */
        px?: number;
        /** Offset from link center perpendicularly to link direction. A fraction of link radius. */
        py?: number;
        /** Whether to rotate link labels in the same direction as link */
        rotateWithLink?: boolean;
        /** X offset from link center in pixels. */
        x?: number;
        /** Y offset from link center in pixels. */
        y?: number;
    }
    export interface ItemsChartSettingsLinkMenu {
        /** Prepare html string to include in the menu. Called whenever a menu is about to be shown. */
        contentsFunction?: (
            /** link data */
            data: ItemsChartDataObjectLink, 
            /** link object */
            node: ItemsChartLink, 
            /** callback function */
            callback: (result: (string|HTMLElement)) => void) => (string|HTMLElement);
        /** Show/hide node/link menu. */
        enabled?: boolean;
        /** Whether to add a view data button to the menu. Useful for debugging. */
        showData?: boolean;
    }
    export interface ItemsChartSettingsLinkStyle {
        cursor?: string;
        /** null or "U", "D", "L", "R" */
        direction?: string;
        fillColor?: string;
        /** The decoration rendered where the link starts. */
        fromDecoration?: string;
        /** Specifies if the link is invisible - thus completely skipping the drawing and hit testing. This can be used, for example, to hide all links
        and showing only ones that meet certain criteria using `linkStyleFunction`. */
        invisible?: boolean;
        items?: Array<ItemsChartSettingsLinkItem>;
        label?: string;
        length?: number;
        lineDash?: Array<number>;
        /** Specifies the width of the line rendered for this link. */
        radius?: number;
        shadowBlur?: number;
        shadowColor?: string;
        shadowOffsetX?: number;
        shadowOffsetY?: number;
        strength?: number;
        /** The decoration rendered where the link ends. */
        toDecoration?: string;
        toPieColor?: string;
        toPieValue?: number;
    }
    export interface ItemsChartSettingsNodeAnchorStyle {
        lineColor?: string;
        lineDash?: Array<number>;
        lineWidth?: number;
        shadowBlur?: number;
        shadowColor?: string;
        shadowOffsetX?: number;
        shadowOffsetY?: number;
    }
    /** Node item. */
    export interface ItemsChartSettingsNodeItem extends ItemsChartSettingsItemsLayerLabelStyle {
        /** X offset from node center. A fraction of node width.
        Value of -1 places the item at the left edge of the node.
        Value of 1 places the item at the right edge of the node. */
        px?: number;
        /** Y offset from node center. A fraction of node height.
        Value of -1 places the item at the top edge of the node.
        Value of 1 places the item at the bottom edge of the node. */
        py?: number;
        /** X offset from node center in pixels. */
        x?: number;
        /** Y offset from node center in pixels. */
        y?: number;
    }
    export interface ItemsChartSettingsNodeMenu {
        /** Buttons to show in node menu. */
        buttons?: Array<string>;
        /** Prepare html string or DOM element to include in the menu. Called whenever a menu is about to be shown. */
        contentsFunction?: (
            /** node data */
            data: ItemsChartDataObjectNode, 
            /** node object */
            node: ItemsChartNode, 
            /** callback function if contents are not immediately available */
            callback: (
                /** New contents to include in the menu */
                result: (string|HTMLElement)) => void) => (string|HTMLElement);
        /** Show/hide node/link menu. */
        enabled?: boolean;
        /** Whether to add a view data button to the menu. Useful for debugging. */
        showData?: boolean;
    }
    export interface ItemsChartSettingsNodeStyle {
        /** Node anchor mode. */
        anchorMode?: ItemsChartNodeAnchorMode;
        anchorStyle?: ItemsChartSettingsNodeAnchorStyle;
        /** Node anchor y position. If not set, initial position will be calculated automatically and conserved.
        The coordinate space is dependant on `anchorMode` value.
        * anchorMode = "Scene" - the value is in scene coordinates.
        * anchorMode = "Display" - the value is in pixels from top-left corner of the chart area. */
        anchorX?: number;
        /** Node anchor y position. If not set, initial position will be calculated automatically and conserved.
        The coordinate space is dependant on `anchorMode` value.
        * anchorMode = "Scene" - the value is in scene coordinates.
        * anchorMode = "Fixed" - the value is in pixels from top-left corner of the chart area. */
        anchorY?: number;
        /** When display="rectangle", this setting determines the width/height ratio of the rectangle.
        The longest edge of the rectangle will be set to "radius" and the shortest will be calculated from this variable. */
        aspectRatio?: number;
        coordinates?: (Array<number>|Array<Array<number>>);
        /** Cursor to show when node is hovered. */
        cursor?: string;
        /** Custom shape settings supplied, if display == "customShape" */
        customShape?: ItemsChartSettingsCustomShape;
        /** Valid values: circle (default), text, roundtext, droplet, rectangle, customShape */
        display?: string;
        fillColor?: string;
        image?: string;
        /** Specifies the image cropping method. Valid values are `false` (disable cropping), `true` (default cropping mode), `"crop"`, `"letterbox"` and `"fit"`. */
        imageCropping?: (boolean|string);
        /** Specifies if the node is invisible - thus completely skipping the drawing and hit testing. This can be used, for example, to hide all nodes
        and showing only ones that meet certain criteria using `nodeStyleFunction`. */
        invisible?: boolean;
        /** Additional items that are rendered on and around the node. */
        items?: Array<ItemsChartSettingsNodeItem>;
        /** The label text that is displayed below the node. Set to an empty string "" to remove the label if it has been added before. */
        label?: string;
        labelStyle?: ItemsChartSettingsItemsLayerLabelStyle;
        lineColor?: string;
        lineDash?: Array<number>;
        lineWidth?: number;
        /** Node opacity. */
        opacity?: number;
        radius?: number;
        shadowBlur?: number;
        shadowColor?: string;
        shadowOffsetX?: number;
        shadowOffsetY?: number;
    }
    export interface ItemsChartSettingsNodesLayerStyle extends ItemsChartSettingsItemsLayerStyle {
        /** Removed object fadeout time. */
        fadeTime?: number;
        /** style for hidden link hints. */
        hiddenLinks?: {
            lineColor?: string;
            lineWidth?: number;
            size?: number;
        };
        /** Link radius auto distribution method. */
        linkAutoScaling?: string;
        /** Link length auto distribution method. */
        linkLengthAutoScaling?: string;
        /** Min and max value of link length before zooming is applied. */
        linkLengthExtent?: [number, number];
        /** Min and max value of link half-width before zooming is applied. */
        linkRadiusExtent?: [number, number];
        /** Link strength auto distribution method. */
        linkStrengthAutoScaling?: string;
        /** Min and max value for link strength. */
        linkStrengthExtent?: [number, number];
        /** Distance between multiple links between two nodes. */
        multilinkSpacing?: number;
        /** Controls automatic node scaling. */
        nodeAutoScaling?: string;
        /** Min and max value of node radius, before zooming is applied. */
        nodeRadiusExtent?: [number, number];
    }
    export interface LinearChartSeriesStackData {
        config: LinearChartSettingsStack;
        data: Array<LinearChartSeriesStackDataItem>;
        name: string;
    }
    export interface LinearChartSeriesStackDataItem {
        config: LinearChartSettingsSeries;
        name: string;
        values: LinearChartSeriesStackDataItemValues;
    }
    export interface LinearChartSeriesStackDataItemValues {
        /** The average value within the range. Only available if the series aggregation is `sum` or `avg`. */
        avg: number;
        change: number;
        count: number;
        first: number;
        last: number;
        max: number;
        min: number;
        sum: number;
    }
    export interface LinearChartSettings extends BaseSettings {
        /** Chart area related settings. */
        area?: LinearChartSettingsArea;
        /** Default series settings for each series rendering type. Use this to configure all series of specific type to get line
        or column chart or combination of them. */
        chartTypes?: {
            /** Series type to show an opening and closing value on top of a total variance. */
            candlestick?: LinearChartSettingsSeriesCandleStick;
            /** Series type to render values as vertical bars. */
            columns?: LinearChartSettingsSeriesColumns;
            /** Series type to connect value points by lines. */
            line?: LinearChartSettingsSeriesLines;
        };
        /** The events used to handle user interaction with UI elements. */
        events?: LinearChartSettingsEvents<BaseChartEventArguments, BaseChartEventArguments>;
        /** Info popup when hovering over columns or lines. Content returned in a form of html and is relevant to context of series hovered. */
        info?: LinearChartSettingsInfoPopup;
        /** A variety of interaction options that includes scrolling, zooming and swipe. */
        interaction?: LinearChartSettingsInteraction;
        legend?: LinearChartSettingsLegend;
        /** Localizeable strings including export type options and useful default buttons used for chart interaction.
        Buttons like to navigate back, set the chart on full screen and others. */
        localization?: LinearChartSettingsLocalization;
        /** Array of series in the chart. Each of the series can be different type, can use different data source and
        aggregation. Additionally, series can be clustered and stacked. */
        series?: Array<LinearChartSettingsSeries>;
        /** The default series used as the chart dominant data. Use settings.series array to specify actual series. */
        seriesDefault?: LinearChartSettingsSeries;
        /** Defines stack settings to use in series. Each property is a stack name and value is stack settings.
        Values stack of each series on top of each other in the specified series order. Those, stacked bars help
        to visualize data that is a sum of parts, each of which is in a series. */
        stacks?: Dictionary<LinearChartSettingsStack>;
        /** Series fill color settings. */
        style?: {
            columnColors?: Array<string>;
            lineColors?: Array<string>;
        };
        /** Map from name to configuration. Currently supported are default and secondary. Default value axis is located on the left hand side of the chart and
        secondary is located on the right hand side. Both can be configured to be either inside or outside the chart. */
        valueAxis?: Dictionary<LinearChartSettingsValueAxis>;
        valueAxisDefault?: LinearChartSettingsValueAxis;
    }
    export interface LinearChartSettingsArea extends BaseSettingsArea {
        /** Area style. */
        style?: LinearChartSettingsAreaStyle;
    }
    export interface LinearChartSettingsAreaStyle extends BaseSettingsAreaStyle {
        /** Style settings when there is no data to display. */
        noData?: {
            fillColor?: string;
            /** Image to show when there is no data to display. */
            image?: string;
        };
        /** Zoom In/Zoom Out animation highlight settings. */
        zoomHighlight?: LinearChartSettingsHighlightStyle;
        /** Zoom highlight style after zoom animation is finished. */
        zoomHighlightInactive?: {
            fillColor?: string;
        };
    }
    export interface LinearChartSettingsCandleStickMotionStyle {
        /** LHOC pattern style when body part excluded */
        bar?: {
            lineColor?: string;
            lineDash?: Array<number>;
            lineWidth?: number;
            shadowBlur?: number;
            shadowColor?: string;
            shadowOffsetX?: number;
            shadowOffsetY?: number;
        };
        /** LHOC pattern style when body part included. Line and shadow properties relevant to candlestick outline including body and shadow part. */
        candlestick?: {
            fillColor?: string;
            /** Candlestick outline color. Including item body outline and shadow stroke style. */
            lineColor?: string;
            lineDash?: Array<number>;
            lineWidth?: number;
            shadowBlur?: number;
            shadowColor?: string;
            shadowOffsetX?: number;
            shadowOffsetY?: number;
        };
    }
    export interface LinearChartSettingsEvents<TArguments extends BaseChartEventArguments, TClickArguments extends BaseChartEventArguments> extends BaseSettingsEvents<TArguments, TClickArguments> {
        /** Function called when chart scrolling animation is finished. */
        onAnimationDone?: (
            /** An empty mouse event. */
            event: BaseMouseEvent, args: TArguments) => void;
    }
    export interface LinearChartSettingsHighlightStyle {
        /** Fade in duration. It will only be used when the highlight is displayed the first time. If highlight of the same category is already
        shown, `fadeCross` will be used instead. */
        fadeIn?: number;
        /** Fade out duration. It will only be used when the highlight is removed and no highlight of the same category is being shown. */
        fadeOut?: number;
        fillColor?: string;
    }
    export interface LinearChartSettingsInfoPopup {
        /** Advanced settings relevant to info popup appearance. */
        advanced?: {
            /** Create custom info contents to display in info popup. */
            contentsFunction?: (
                /** Data for the info popup */
                data: Array<LinearChartSeriesStackData>, 
                /** The configuration of the series object currently under the cursor */
                series: any, 
                /** The range for which the info popup is being built. */
                range: [number, number]) => string;
            /** Controls how the selection for the info popup is created and what data is displayed. */
            scope?: string;
            /** Specifies if the default header should be included. If set to `false`, the `contentsFunction` should be used to return the header as well. */
            showHeader?: boolean;
            /** Whether to show only the series under cursor in info popup. If `scope` is set to `value` then the info popup will be empty unless the pointer
            hovers over the value bar/line. */
            showOnlyHoveredSeries?: boolean;
        };
        /** List of aggregations that will be shown in the info popup for each series. If none are specified, info popup displays the aggregation
        that is specified in the series data configuration.
        Available aggregations: sum (default), count, first, last, min, max, avg, change */
        aggregations?: Array<string>;
        /** Show/hide info popup */
        enabled?: boolean;
        /** Specifies the position of the info popup. */
        position?: string;
        /** Whether to show series with no data in hovered time period. */
        showNoData?: boolean;
        /** Info popup style. */
        style?: {
            /** Style used to highlight the values that are shown in the info popup. */
            highlight?: LinearChartSettingsHighlightStyle;
        };
        /** Prepare custom format values to display in info popup. */
        valueFormatterFunction?: (
            /** object containing {sum, min, max, first, last, count, avg, change} */
            values: LinearChartSeriesStackDataItemValues, 
            /** series object for the value */
            series: LinearChartSettingsSeries) => string;
    }
    export interface LinearChartSettingsInteraction extends BaseSettingsInteraction {
        /** Configurable settings to manage scroll interaction. */
        scrolling?: LinearChartSettingsInteractionScrolling;
        /** Sensitivity of Left/Right/Up/Down swipes. Note that `scrolling.swipePageFlipping` must be `true` for this to have any effect. */
        swipeSensitivity?: number;
        /** Configurable settings to enhance and alleviate zoom interaction. */
        zooming?: {
            /** If set to `false`, all zoom gestures will be disabled, irrespective of how the other properties are configured.
            Note that the user will still be able to zoom by drill-down (clicking on values) and by using toolbar. */
            enabled?: boolean;
            /** Whether to zoom by two finger pinch. */
            fingers?: boolean;
            /** Max zoom difference when using pinch gesture. */
            fingersMaxZoom?: number;
            /** Chart is zoomed in or out by this factor when pressing Up or Down keys. */
            keyboardFactor?: number;
            /** Mouse zooming sensitivity. Note that bigger values correspond to faster zooming. */
            sensitivity?: number;
            /** Whether to zoom by swiping up or down. */
            swipe?: boolean;
            /** How far pointer must be moved up or down in pixels before zooming activates. */
            upDownTreshold?: number;
            /** Whether to zoom by mouse wheel. */
            wheel?: boolean;
            /** Zoomed area is highlighted if zoom is changed more number of times than this. */
            zoomHighlightThreshold?: number;
        };
    }
    export interface LinearChartSettingsInteractionScrolling {
        /** Enables/Disables scrolling. */
        enabled?: boolean;
        /** How far to scroll when Left or Right keys are pressed. Multiples of chart width. */
        keyboardScrollingFactor?: number;
        /** Scrolling friction coefficient (chartWidth/ms^2). */
        kineticFriction?: number;
        /** Whether to use scrolling by full page instead of kinetic scrolling when using swipes (instead of dragging the chart). */
        swipePageFlipping?: boolean;
    }
    export interface LinearChartSettingsLegend extends BaseSettingsLegend {
        /** Settings to configure the legend marker appearance if disabled series corresponded. */
        advanced?: LinearChartSettingsLegendAdvanced;
        /** Legend enclosing panel settings. */
        panel?: BaseSettingsLegendPanel;
    }
    export interface LinearChartSettingsLegendAdvanced extends BaseSettingsLegendAdvanced {
        /** Style for legend entry when a disabled series is displayed. */
        disabledSeries?: {
            fillColor?: string;
            lineColor?: string;
            textColor?: string;
        };
    }
    export interface LinearChartSettingsLocalization extends BaseSettingsLocalization {
        /** Text to show when there is no data to display. */
        noDataLabel?: string;
        /** Strings used in toolbars. */
        toolbar?: LinearChartSettingsLocalizationToolbar;
        /** Default name for series shown in info popup and legend. Note that info popup will not use it if only a single series is being used. */
        unnamedSeries?: string;
        /** Map from unit prefix to multiplier. Used for value axis formatting. */
        valueUnits?: Dictionary<number>;
    }
    export interface LinearChartSettingsLocalizationToolbar extends BaseSettingsLocalizationToolbar {
        /** Linear mode button text. */
        linButton?: string;
        /** Lin/Log button title. */
        linLogTitle?: string;
        /** Logarithmic mode button text. */
        logButton?: string;
    }
    export interface LinearChartSettingsSeries {
        /** Cluster identifier. Columns with the same cluster id will be placed in same cluster. */
        cluster?: string;
        /** Data manipulation settings used for default series. */
        data?: LinearChartSettingsSeriesData;
        /** Enable/disable series. */
        enabled?: boolean;
        /** A field that can be used to store any additional data with the series object. This can be useful in situations when the
        data is required in event handlers, such as `onClick` when the event arguments contain the configuration of the series that
        was hovered or clicked. This value is copied by reference. */
        extra?: any;
        id?: string;
        /** Name to show in the info popup. The same value is also used in the legend unless `nameLegend` is also set. */
        name?: string;
        /** Name to show in the legend. If not specified, value from the `name` property is shown. */
        nameLegend?: string;
        /** Whether to show the series name in legend and info popup. */
        showInLegend?: boolean;
        /** Stack identifier. Series with same stack ID are placed in the same stack. Define a stack with the same identifier to tune the stack. */
        stack?: string;
        /** Custom style based on series type. See the documentation of the derived classes for available properties. */
        style?: LinearChartSettingsSeriesStyle;
        /** Series type. */
        type?: string;
        /** ID of value axis that this series will use.
        Maps to a configuration specified in [valueAxis](full-reference/LinearChartSettings.html#doc_valueAxis) property. */
        valueAxis?: string;
    }
    export interface LinearChartSettingsSeriesCandleStick extends LinearChartSettingsSeries {
        /** Info popup localizeable strings */
        localization?: {
            close?: string;
            high?: string;
            low?: string;
            open?: string;
        };
        /** Custom style based on series type. See the documentation of the derived classes for available properties. */
        style?: LinearChartSettingsSeriesCandleStickStyle;
    }
    export interface LinearChartSettingsSeriesCandleStickStyle extends LinearChartSettingsSeriesStyle {
        /** Item style when open is higher than close */
        decrease?: LinearChartSettingsCandleStickMotionStyle;
        /** Item style when open is smaller than close */
        increase?: LinearChartSettingsCandleStickMotionStyle;
        /** Item left and right padding */
        padding?: [number, number];
        /** Candlestick item representation pattern where difference is wethter to show body part */
        pattern?: string;
    }
    export interface LinearChartSettingsSeriesColumns extends LinearChartSettingsSeries {
        /** Default column style. */
        style?: LinearChartSettingsSeriesColumnsStyle;
        /** Controls if and how the value labels for each column is displayed on the chart. */
        valueLabels?: LinearChartSettingsValueLabels;
    }
    export interface LinearChartSettingsSeriesColumnsStyle extends LinearChartSettingsSeriesStyle {
        /** Depth for column. Use it to achieve 3D effect. */
        depth?: number;
        /** Brightness applied to depth components. */
        depthBrightness?: number;
        /** A linear gradient for color change along a line between the column bottom and upper side.
        Specify "1" for no gradiend. Values 0..` will make the bottom part of columns slightly darker. */
        gradient?: number;
        /** Outline color. */
        lineColor?: string;
        /** Minimum height of a column in px. */
        minHeight?: number;
        /** Padding for column. 0th element - left padding, 1st element - right padding. */
        padding?: [number, number];
        /** Shadow blur effect range. */
        shadowBlur?: number;
    }
    export interface LinearChartSettingsSeriesData {
        /** Prepare displaying value after data aggregation. */
        aggregatedValueFunction?: (
            /** aggregated value that is about to be displayed */
            value: number, 
            /** time display unit time stamp in ms */
            time: number, 
            /** display unit */
            units: string) => any;
        /** Aggregation function to use. Used when data source does not provide data in needed display unit.
        Note that when `avg` is used, you should provide `countIndex` so that the calculation is correct even across multiple units. */
        aggregation?: string;
        /** Method used to fill in time intervals that have no data. Used only for line series. */
        noDataPolicy?: string;
        /** Data source to use if multiple data sources are available. */
        source?: string;
        /** Retrieves the individual value used for calculations from the data array. This is an alternative for specifying `index`. */
        valueFunction?: (
            /** entry in data values array where the value at index 0 is the timestamp */
            data: Array<number>) => number;
    }
    export interface LinearChartSettingsSeriesLieneStyle extends LinearChartSettingsSeriesStyle {
        /** Marker highlight data points on line. They can be in different sizes, shapes and colors. */
        marker?: {
            /** Marker fill color. Note that it must be set to the preferred color in order to display them. */
            fillColor?: string;
            /** Specify the shape of markers on the line. */
            shape?: string;
            /** The width of the marker. In case when circle - interpreted as the diameter. */
            width?: number;
        };
        /** Shadow blur radius. */
        shadowBlur?: number;
        /** Whether to draw smoothed line. */
        smoothing?: boolean;
        /** Whether to draw the line using horizontal segments instead of oblique. */
        steps?: boolean;
    }
    export interface LinearChartSettingsSeriesLines extends LinearChartSettingsSeries {
        /** Default style for line type series. */
        style?: LinearChartSettingsSeriesLieneStyle;
        /** Controls if and how the value labels for each line is displayed on the chart. */
        valueLabels?: LinearChartSettingsValueLabels;
    }
    export interface LinearChartSettingsSeriesStyle {
        depth?: number;
        /** Fill color. */
        fillColor?: string;
        /** Fill gradient. Allows building a gradient fill, bound to values. Contains array of value-color pairs.
        For example: [[-20, 'rgba(0,0,255,0.4)'],[30,'rgba(255,0,0,0.7)']]. */
        fillGradient?: GradientDefinition;
        lineColor?: string;
        /** Array of line dash pattern to have a dashed line. The array contains length of dash followed by length of space in pixels.
        A sequence of multiple dash-space values is supported. */
        lineDash?: Array<number>;
        /** Width of the line. */
        lineWidth?: number;
        /** Shadow color of column. If undefined, no shadow will be applied. Leave empty to use default shadow or set your own shadow color. */
        shadowColor?: string;
        /** Shadow direction, x component. */
        shadowOffsetX?: number;
        /** Shadow direction, y component. */
        shadowOffsetY?: number;
    }
    export interface LinearChartSettingsStack {
        /** The display name in info popup. */
        name?: string;
        /** Whether to separate negative values. */
        separateNegativeValues?: boolean;
        /** Different representation of stacked series values. */
        type?: string;
    }
    export interface LinearChartSettingsValueAxis {
        /** Whether to show vertical line along value axis */
        axisLine?: boolean;
        /** Show/hide value axis. */
        enabled?: boolean;
        /** Whether to show horizontal grid lines. Specifying `null` means that only the first value axis will show the horizontal lines,
        all other axis will not. */
        hgrid?: boolean;
        /** Whether to use logarithmic scale. */
        logScale?: boolean;
        /** Fixed maximum value for value axis. If not set it will be computed automatically from visible data and zeroLine settings. */
        maxValue?: number;
        /** Fixed minimum value for value axis. If not set it will be computed automatically from visible data and zeroLine settings. */
        minValue?: number;
        /** Location of the value axis. */
        position?: string;
        /** The animation easing function. */
        scaleAdjustmentAnimation?: string;
        /** Scale adjustment animation duration. */
        scaleAdjustmentAnimationDelay?: number;
        /** This tolerance specifies how big that part is as fraction of total chart height. When part of the vertical space
        is not used chart scale is automatically adjusted. */
        scaleAdjustmentTolerance?: number;
        /** Smallest difference between two labels. For example, use 1 to disallow the labels to go into fractions. */
        scaleMinStep?: number;
        /** Difference between two values on the axis. If not set (default), will be automatically calculated from chart height, min,
        max value and style.labelSpacing. */
        scaleStep?: number;
        /** Value axis side in chart */
        side?: string;
        /** Fixed width of the value axis. If not set it will be computed automatically from font size. */
        size?: number;
        /** Style for parts of value axis. */
        style?: {
            /** Rendering style for the vertical line along value axis. */
            axisLine?: BaseSettingsLineStyle;
            /** Base line settings. It is the horizontal line showing zero value. */
            baseLine?: LinearChartSettingsValueAxisBaseLineStyle;
            /** Horizontal grid lines. */
            hgrid?: BaseSettingsLineStyle;
            /** Distance between value axis labels. */
            labelSpacing?: number;
            /** Value axis tick lines style. */
            tick?: BaseSettingsLineStyle;
            /** Title text for the value axis. */
            title?: LinearChartSettingsValueAxisTitleStyle;
            /** Value axis label rendering settings. */
            valueLabel?: LinearChartSettingsValueAxisValueLabelStyle;
        };
        /** Provides the ability to draw threshold guidelines and fill background areas for certain values. For example, this can be used
        to highlight an area that exceeds certain limit. */
        thresholds?: Array<LinearChartSettingsValueAxisThreshold>;
        /** Title text for the value axis. */
        title?: string;
        /** Prepare custom format values to display in value axis. If using this, also set the `size` parameter to accommodate your label size. */
        valueFormatterFunction?: (
            /** numerical value to display */
            value: number, 
            /** name of multiplier - 'K' for thousands, 'M' for millions */
            unitName: string, 
            /** multiplier value - 1000 for thousands, 1 000 000 for millions */
            unitValue: number, 
            /** value string that is displayed by default */
            name: string) => string;
        /** Zero line. */
        zeroLine?: string;
    }
    export interface LinearChartSettingsValueAxisBaseLineStyle extends BaseSettingsLineStyle {
        depthColor?: string;
        lineDepth?: number;
    }
    export interface LinearChartSettingsValueAxisThreshold {
        /** Specifies the bottom bound of the threshold area. */
        from?: number;
        /** Threshold placement in relation to series data. */
        position?: string;
        /** Describes the visual style for the threshold guidelines and area. */
        style?: {
            /** Specifies the fill color for the threshold area. If multiple areas overlap, this should specify an `rgba()` color with transparency.
            If 'null' fill color will not be applied. */
            fillColor?: string;
            /** Specifies the line color for the upper and lower bounds. If `null`, the lines will not be drawn. */
            lineColor?: string;
            /** Array of line dash pattern to have a dashed line. The array contains length of dash followed by length of space in pixels.
            A sequence of multiple dash-space values is supported. */
            lineDash?: Array<number>;
            /** Specifies the width of the boundary lines. */
            lineWidth?: number;
        };
        /** Specifies the upper bound of the threshold area. */
        to?: number;
    }
    export interface LinearChartSettingsValueAxisTitleStyle extends BaseSettingsLabelStyle, BaseSettingsTextStyle {
        alignment?: string;
        reverseDirection?: boolean;
    }
    export interface LinearChartSettingsValueAxisValueLabelStyle extends BaseSettingsLabelStyle, BaseSettingsTextStyle {
    }
    export interface LinearChartSettingsValueLabels {
        /** Prepare custom content to display in value label along with numeric value.
        If this callback is not defined, then floating value is formatted with two digits after the decimal point and integer values are formatted without decimal digits.
        If this callback returns `null` or `undefined` for the given value, it won't be created. Note that `null` values are never passed to the callback. Usage example:
        ```javascript
        contentsFunction: function(value) { return value.toFixed(2) + "$"; }
        ``` */
        contentsFunction?: (
            /** The value which has to be formatted. */
            value: number) => string;
        /** Whether to show series data labels. */
        enabled?: boolean;
        /** The font size will be used as defined in `series.valueLabel.style`, however, if the available space is too narrow, font size will be gradually reduced to the minimum font size.
        If the minimum font size still does not fit, the values won't be displayed. */
        minFontSize?: number;
        /** Value label position in perspective to the series data point. Note that in upcoming versions
        all of the inside options will automatically reduce the font size (based on `minFontSize` setting)
        in situations when the height of the column or line area does not provide enough room for full-size
        labels.
        ![Value label possible positions](images/valueLabelPosition.png) */
        position?: string;
        /** Data label text style. */
        style?: BaseSettingsLabelStyle;
    }
    export interface NetChartBarSettingsLocalizationToolbar extends BaseSettingsLocalizationToolbar {
        fitButton?: string;
        fitTitle?: string;
        freezeButton?: string;
        freezeTitle?: string;
        rearrangeButton?: string;
        rearrangeTitle?: string;
        unfreezeTitle?: string;
    }
    export interface NetChartBarSettingsToolbar extends BaseSettingsToolbar {
        /** Whether to show the zoom slider control. */
        zoomControl?: boolean;
    }
    /** Describes the base properties shared between all events raised by the different charts. */
    export interface NetChartChartClickEventArguments extends NetChartChartEventArguments {
        clickItem: BaseLabel;
        clickLink: ItemsChartLink;
        clickNode: ItemsChartNode;
    }
    /** Describes the base properties shared between all events raised by the different charts. */
    export interface NetChartChartEventArguments extends ItemsChartChartEventArguments {
        links: Array<ItemsChartLink>;
        nodes: Array<ItemsChartNode>;
    }
    export interface NetChartDataObject extends BaseDataErrorResponse {
        links: Array<NetChartDataObjectLink>;
        nodes: Array<NetChartDataObjectNode>;
    }
    export interface NetChartDataObjectLink extends ItemsChartDataObjectLink {
    }
    export interface NetChartDataObjectNode extends ItemsChartDataObjectNode {
        value?: number;
        x?: number;
        y?: number;
    }
    export interface NetChartSettings extends ItemsChartSettings {
        /** Chart area related settings. */
        area?: NetChartSettingsArea;
        /** Settings used to load data into chart. Customise preferred data source feeding methods.
        You can use one of these options: url, data function, preloaded data. */
        data?: Array<NetChartSettingsData>;
        /** The events used to handle user interaction with UI elements. */
        events?: BaseSettingsEvents<NetChartChartEventArguments, NetChartChartClickEventArguments>;
        /** Configurable conditions to filter the raw data values for subset of drawing nodes and links. */
        filters?: {
            /** Determine if link can be displayed. Invoked whenever a link is about to be shown or its data has changed.
            Only links that have been allowed by nodeFilter for both end nodes will be passed here. */
            linkFilter?: (
                /** link data object */
                linkData: NetChartDataObjectLink, 
                /** data object representing node where the link begins */
                fromNodeData: NetChartDataObjectNode, 
                /** data object representing node where the link ends */
                toNodeData: NetChartDataObjectNode) => boolean;
            /** Function called whenever there is more than one link between two nodes. Only links that were allowed by nodeFilter, linkFilter and nodeLinksProcessor
            will be passed here. The function can return either some of the original links, or create completely new links.
            In the latter case, link IDs MUST be unique (links passed in are guaranteed to have unique IDs). */
            multilinkProcessor?: (
                /** array of link data objects */
                arrayOfLinkData: Array<NetChartDataObjectLink>, 
                /** data object representing node where the links begins */
                fromData: NetChartDataObjectNode, 
                /** data object representing node where the links ends */
                toData: NetChartDataObjectNode) => (NetChartDataObjectLink|Array<NetChartDataObjectLink>);
            /** Determine if node can be displayed. Invoked whenever a node or one of its links is about to be shown, or if data for the node (or its links) has changed. */
            nodeFilter?: (
                /** Node data object */
                nodeData: NetChartDataObjectNode, 
                /** Unfiltered array of link data objects (linkFilter/nodeLinksProcessor/multilinkProcessor have not been applied) */
                arrayOfLinkData: Array<NetChartDataObjectLink>) => boolean;
            /** From links that were allowed by nodeFilter and linkFilter, select the ones that will be displayed. This is basically a bulk version of linkFilter.
            It is also allowed to return a completely new set of links, however link IDs MUST be unique in this case
            (links passed in are guaranteed to have unique IDs). */
            nodeLinksProcessor?: (
                /** Node data object */
                nodeData: NetChartDataObjectNode, 
                /** Array of link data objects. All links are connected to the node. Only links that were allowed by nodeFilter/linkFilter will be passed here.
                MultilinkProcessor has not been applied yet. */
                links: Array<NetChartDataObjectLink>) => Array<NetChartDataObjectLink>;
        };
        /** Customise chart resize handles or animation duration settings. */
        interaction?: NetChartSettingsInteraction;
        /** Adjustable settings to get desired net chart layout style and animation while and before interacting. */
        layout?: NetChartSettingsLayout;
        /** The chart legend representing classes attached to nodes or links. 
        The legend will display the visual styles specified in `style.nodeClasses` and `style.linkClasses` - by default these are not defined
        so the legend will be empty. */
        legend?: NetChartSettingsLegend;
        /** Localizeable strings including export type options and useful default buttons used for chart interaction.
        Buttons like to navigate back, set the chart on full screen and others. */
        localization?: NetChartSettingsLocalization;
        /** Settings for NetChart navigation (expanding/collapsing/focusing/unfocusing/showing/hiding). The main setting is "mode" which determines the overall
        algorithm for navigation. Other parameters can tweak this algorithm, but not all parameters apply to all algorithms. */
        navigation?: NetChartSettingsNavigation;
        /** Configurable node menu with option to specify a range of displaying buttons. */
        nodeMenu?: ItemsChartSettingsNodeMenu;
        /** Chart style settings. */
        style?: ItemsChartSettingsNodesLayerStyle;
        /** Theme to apply. You can either use this to share configuration objects between multiple charts or use one of the predefined
        themes. */
        theme?: NetChartSettings;
        /** Adjustable settings to manage default and custom toolbar items, as well as toolbar overall appearance. */
        toolbar?: NetChartBarSettingsToolbar;
    }
    export interface NetChartSettingsArea extends BaseSettingsArea {
        /** The center of the chart. Fraction of chart width. 0 = left side, 1 = right side. */
        centerX?: number;
        /** The center of the chart. Fraction of chart height, 0 = top, 1 = botom. */
        centerY?: number;
        /** Inner bottom padding, nodes will avoid this area.
        If the value is <= 1 then the value represents the fraction from the chart width. 
        Otherwise it represents the padding value in pixels. */
        paddingBottom?: number;
        /** Inner left padding, nodes will avoid this area.
        If the value is <= 1 then the value represents the fraction from the chart width. 
        Otherwise it represents the padding value in pixels. */
        paddingLeft?: number;
        /** Inner right padding, nodes will avoid this area.
        If the value is <= 1 then the value represents the fraction from the chart width. 
        Otherwise it represents the padding value in pixels. */
        paddingRight?: number;
        /** Inner top padding, nodes will avoid this area.
        If the value is <= 1 then the value represents the fraction from the chart width. 
        Otherwise it represents the padding value in pixels. */
        paddingTop?: number;
    }
    export interface NetChartSettingsData extends ItemsChartSettingsData {
        /** Load more chart data. */
        dataFunction?: (
            /** node IDs */
            nodes: Array<string>, 
            /** callback function to execute when data arrived correctly */
            success: (data: NetChartDataObject) => void, 
            /** callback function to execute when error occure while loading data */
            fail: (result: BaseDataErrorResponse) => void) => void;
        /** Provides the ability to embed chart data directly into the chart configuration.
        Data can be represented by an JavaScript object or a JSON string. */
        preloaded?: (string|NetChartDataObject);
    }
    export interface NetChartSettingsInteraction extends ItemsChartSettingsInteraction {
        /** The ability to rotate the chart with the pinch gesture, using 2 fingers */
        rotation?: {
            /** Enables/disables chart rotation via the multitouch gesture events */
            fingers?: boolean;
        };
        /** Select node to expand it or getting path. */
        selection?: NetChartSettingsInteractionSelection;
        /** Zoom in or out by swiping up or down with mouse scroll pad or by using the Zoom-out feature at the top left. */
        zooming?: NetChartSettingsInteractionZooming;
    }
    export interface NetChartSettingsInteractionSelection extends ItemsChartSettingsInteractionSelection {
    }
    export interface NetChartSettingsInteractionZooming extends ItemsChartSettingsInteractionZooming {
        /** Zoom value limits when user zooms in and out. Contains array of [min, max] values. If the network is big, the min value
        is adjusted so that whole network can be shown. */
        autoZoomExtent?: [number, number];
        /** The acceleration of scene movement, when trying to contain all nodes within the view,
        when autozoom is enabled. Increasing the value decreases latency, and makes the animation
        more responsive. Decreasing the value makes the animation more fluid */
        autoZoomPositionEllasticity?: number;
        /** Fraction of the chart to use in auto zoom mode. This governs the white space area around network in auto zoom mode. */
        autoZoomSize?: number;
        /** Auto zoom mode on chart initialization. */
        initialAutoZoom?: string;
        /** Zoom value limits while in auto-zoom mode. Contains array of [min, max] values. */
        zoomExtent?: [number, number];
    }
    export interface NetChartSettingsLayout {
        /** Advanced chart settings. Be advised that they are subject to change, backwards compatibility is not guaranteed. */
        advanced?: {
            adaptiveFreezeTreshold?: number;
        };
        /** Whether to fit network in aspect ratio of chart viewport. Useful for small networks that always fit in chart and are not intended to be zoomed in / out. */
        aspectRatio?: boolean;
        /** Whether to perform global layout on network changes. Use it for better node placement at the cost of chart slowdown on network changes. */
        globalLayoutOnChanges?: boolean;
        /** Maximum time to wait for incremental layout to be completed. Note that bigger value will get nicer placement on network updates at the cost of longer delay. */
        incrementalLayoutMaxTime?: number;
        /** Maximum time to wait for initial layout to be completed. Note that bigger value will get nicer placement of big networks at the cost of long initial delay. */
        initialLayoutMaxTime?: number;
        /** Dynamic layout can be stopped faster if no more movement is detected. */
        layoutFreezeMinTimeout?: number;
        /** Dynamic layout is stopped after user is inactive for this time. */
        layoutFreezeTimeout?: number;
        /** Layout mode. */
        mode?: string;
        /** Desired distance between nodes. */
        nodeSpacing?: number;
        /** Desired vertical distance between node rows in the hierarchy layout. */
        rowSpacing?: number;
    }
    export interface NetChartSettingsLegend extends BaseSettingsLegend {
        /** Legend enclosing panel settings. */
        panel?: BaseSettingsLegendPanel;
    }
    export interface NetChartSettingsLocalization extends BaseSettingsLocalization {
        /** Node/link menu by using localizeable strings. */
        menu?: {
            collapse?: string;
            dynaminc?: string;
            expand?: string;
            fixed?: string;
            focus?: string;
            hide?: string;
            unfocus?: string;
        };
        /** Strings used in toolbars. */
        toolbar?: NetChartBarSettingsLocalizationToolbar;
    }
    /** Settings for NetChart navigation (expanding/collapsing/focusing/unfocusing/showing/hiding). The main setting is `mode` which determines the overall
    algorithm for navigation. Other parameters can tweak this algorithm, but not all parameters apply to all algorithms. */
    export interface NetChartSettingsNavigation {
        /** Determines what happens if the user has reached maximum number of focus nodes (`numberOfFocusNodes`) and focuses another node.
        If this setting is `true`, then the least recently focused node will be unfocused. If this setting is `false`, then the user
        will not be able to focus the node. _Used by modes: all modes_ */
        autoUnfocus?: boolean;
        /** Whether to auto-zoom to a node when it is focused. _Used by modes: all modes_ */
        autoZoomOnFocus?: boolean;
        /** If focusing a node would display several levels of nodes (due to `focusNodeExpansionRadius` or `focusNodeTailExpansionRadius`), each level is shown after
        the specified delay (milliseconds). Set to 0 to disable. _Used by modes: `focusnodes`_ */
        expandDelay?: number;
        /** Whether to expand node on click. _Used by modes: all modes_ */
        expandOnClick?: boolean;
        /** If set to true, nodes and links with [relevance](full-reference/ItemsChartNode.html#doc_relevance) < 1 will be drawn with a smaller radius and a faded out
        color (both multiplied by [relevance](full-reference/ItemsChartNode.html#doc_relevance)). _Used by modes: `focusnodes`_ */
        focusAutoFadeout?: boolean;
        /** Number of "levels" to automatically expand around the most recently focused node. If set to 1, all nodes directly linked to the focused node will be shown.
        If set to 2, all nodes directly connected to these nodes will be shown as well. Etc. Also used for calculating
        [node relevance](full-reference/ItemsChartNode.html#doc_relevance). _Used by modes: `focusnodes`_ */
        focusNodeExpansionRadius?: number;
        /** Similar to `focusNodeExpansionRadius`, but for the least recently focused node. This allows to create an effect, where the most recently focused node has
        many expanded nodes around it, while the least recently node has only a few (or vice versa). Intermediate focused nodes will have their expansion radius
        linearly interpolated between `focusNodeExpansionRadius` and `focusNodeTailExpansionRadius`. Also used to calculate
        [node relevance](full-reference/ItemsChartNode.html#doc_relevance). _Used by modes: `focusnodes`_ */
        focusNodeTailExpansionRadius?: number;
        /** Initially visible/focused nodes. Array of node identifiers. The precise effect depends on the navigation mode.
        * For `manual` this specifies the initially visible nodes and must contain at least 1 node.
        * For `showall` this specifies which nodes to show first, and other nodes are then requested recursively from these until all nodes are visible.
        * If this setting is left empty or `null`, the chart will directly request ALL nodes (this is more efficient if your data source supports it).
        * For `focusnodes` this specifies the initially focused nodes. The count of node IDs in this array must be between `minNumberOfFocusNodes` and
           `numberOfFocusNodes`
        
        _Used by modes: all modes_ */
        initialNodes?: Array<string>;
        /** Minimum number of focused nodes. Prevents user from unfocusing nodes if there are `minNumberOfFocusNodes` or less nodes focused.
        If the `focusnodes` navigation is used, this setting has a minimum value of 1. _Used by modes: all modes_ */
        minNumberOfFocusNodes?: number;
        /** Navigation mode - the algorithm that determines the expanding/collapsing logic. */
        mode?: string;
        /** Maximum number of focused nodes. The `autoUnfocus` setting determines what happens when more nodes are focused.  _Used by modes: all modes_ */
        numberOfFocusNodes?: number;
    }
    /** Describes the base properties shared between all events raised by the different charts. */
    export interface PieChartChartClickEventArguments extends PieChartChartEventArguments {
        clickPie: PieChartPie;
        /** Contains the slice that was clicked. Note that this will also be populated when the label
        of the slice is clicked. */
        clickSlice: PieChartSlice;
    }
    /** Describes the base properties shared between all events raised by the different charts. */
    export interface PieChartChartEventArguments extends BaseChartEventArguments {
        count: number;
        /** Contains the label object if one is currently hovered, `null` otherwise. */
        label: BaseLabel;
        offset: number;
        pie: PieChartPie;
        selection: Array<PieChartSlice>;
        /** Contains the currently hovered slice. Note that this will also be populated when the label
        of the slice is clicked. */
        slice: PieChartSlice;
    }
    export interface PieChartDataObject extends PieChartDataObjectCommon {
        /** Styles specific to the particular slice. */
        style?: PieChartSettingsSliceStyle;
        value: number;
    }
    export interface PieChartDataObjectCommon extends BaseDataObjectBase {
        afterSum?: number;
        beforeSum?: number;
        id?: string;
        limit?: number;
        name?: string;
        offset?: number;
        subvalues?: Array<PieChartDataObject>;
        sum?: number;
        total?: number;
    }
    export interface PieChartDataObjectRoot extends PieChartDataObjectCommon, BaseDataErrorResponse {
        subvalues: Array<PieChartDataObject>;
    }
    export interface PieChartPie {
        active: boolean;
        activeSliceId: string;
        allSlices: Array<PieChartSlice>;
        background: boolean;
        brightness: number;
        colorDistribution: string;
        count: number;
        data: PieChartPieData;
        endAngle: number;
        fillColor: string;
        getActiveSlice(): PieChartSlice;
        id: string;
        innerRadius: number;
        loading: boolean;
        offset: number;
        othersSlice: PieChartSlice;
        parentSlice: PieChartSlice;
        previousSlice: PieChartSlice;
        radius: number;
        removed: boolean;
        scrollOffset: number;
        sliceColors: Array<string>;
        slices: Array<PieChartSlice>;
        startAngle: number;
        total: number;
        visibleFraction: number;
        x: number;
        y: number;
    }
    /** retrieving and caching raw data */
    export interface PieChartPieData {
        afterFraction: number;
        /** sum of values before offset */
        afterSum: number;
        beforeFraction: number;
        /** sum of values after values */
        beforeSum: number;
        done: boolean;
        markDone(): void;
        offset: number;
        total: number;
        /** max number of items available */
        totalCount: number;
        values: Array<PieChartDataObject>;
        valuesSum: number;
    }
    export interface PieChartSettings extends BaseSettings {
        /** Advanced chart settings. Be advised that they are subject to change, backwards compatibility is not guaranteed. */
        advanced?: PieChartSettingsAdvanced;
        /** Settings used to load data into chart. Customise preferred data source feeding methods.
        You can use one of these options: url, data function, preloaded data. */
        data?: Array<PieChartSettingsData>;
        /** The events used to handle user interaction with UI elements. */
        events?: PieChartSettingsEvents;
        /** Configurable conditions to filter the raw data values for subset of drawing slices. */
        filters?: {
            /** Function to filter visible slices. */
            sliceFilter?: (
                /** slice filtering data */
                sliceData: PieChartDataObject) => boolean;
        };
        /** Slice icons as an additional element of style to highlight each individual slice or groups. */
        icons?: {
            /** Whether to render icons if smaller than min value of sizeExtent. */
            autohideWhenTooSmall?: boolean;
            /** Icon placement method */
            placement?: string;
            /** Min and max value of icon size. The icon size is automatically computed from available space. */
            sizeExtent?: [number, number];
        };
        /** Rising content field while hovering over slice. Content returned in a form of html and is relevant to context of slice hovered. */
        info?: {
            /** Prepare custom info popup contents. May return null and call callback(contents) later. */
            contentsFunction?: (
                /** slice data including subvalues */
                sliceData: any, 
                /** slice object to apply predefined content */
                slice: any, 
                /** function called to return predefined content */
                callback: (result: string) => void) => string;
            /** Show/hide info popup. */
            enabled?: boolean;
        };
        /** Configurable interactivity options to navigate among the slices and pie levels to facilitate analysis of the grouped data in different ways. */
        interaction?: PieChartSettingsInteraction;
        /** Label settings related to internal and external slice text and their connectors. */
        labels?: PieChartSettingsLabels;
        /** The chart legend by additional interactivity to navigate between the slices or hilight each of them. Note that click on entries 
        acts the same way as click on slice and is dependant of chart interaction mode. */
        legend?: PieChartSettingsLegend;
        /** Localizeable strings including export type options and useful default buttons used for chart interaction.
        Buttons like to navigate back, set the chart on full screen and others. */
        localization?: PieChartSettingsLocalization;
        /** Settings to specify initial pie and visible slice offset once the page loaded. */
        navigation?: {
            /** Initial pie drilldown to show. For example ['', 'Firefox', 'Firefox 2.5'] denotes to various browsers grouped by versions. */
            initialDrilldown?: Array<string>;
            /** Initial offset, number of items from start. */
            initialOffset?: number;
        };
        /** Data values arranged in a circular manner. */
        pie?: {
            /** Whether to adapt pie outer radius dynamically to allow enough space for labels. */
            adaptiveRadius?: boolean;
            /** Hovered pie background style. */
            backgroundHoveredStyle?: PieChartSettingsPieStyle;
            /** Pie background style. */
            backgroundStyle?: PieChartSettingsPieStyle;
            /** Pie center margin. */
            centerMargin?: number;
            /** Pie depth - used for raised theme. */
            depth?: number;
            /** End angle of the pie. */
            endAngle?: number;
            /** Pie inner radius. Inner pies are drawn inside this radius. If the value is less than 1, it is multiplied
            by current pie radius to get inner radius. Otherwise it represents the inner radius in pixels. */
            innerRadius?: number;
            /** Inner radius is extended to radius * innerRadiusWhenDrilldown when drilled down. */
            innerRadiusWhenDrilldown?: number;
            /** Pie margin. */
            margin?: number;
            /** Pie style when no data is present. */
            noDataStyle?: PieChartSettingsPieStyle;
            /** Pie outer margin. */
            outerMargin?: number;
            /** Pie outer radius. If not specified, the radius is determined automatically. */
            radius?: number;
            /** Whether to show inner pies on chart. */
            showInnerPies?: boolean;
            /** Whether to show inner pies on exported image. */
            showInnerPiesExport?: boolean;
            /** Start angle of the pie. */
            startAngle?: number;
            /** Default pie rendering style. */
            style?: PieChartSettingsPieStyle;
            /** Dynamically determine pie style from data. */
            styleFunction?: (
                /** pie to apply predefined style */
                pie: PieChartPie) => void;
            /** Default pie rendering theme. */
            theme?: string;
            /** Center X coordinate of the pie chart.
            If the value is 'null' - coordinate is calculated automatically.
            If the value is >1 - it specifies the exact x value in pixels.
            If the value is <=1 - it specifies a fraction of chart width. */
            x?: number;
            /** Center Y coordinate of the pie chart.
            If the value is 'null' - coordinate is calculated automatically.
            If the value is >1 - it specifies the exact x value in pixels.
            If the value is <=1 - it specifies a fraction of chart height. */
            y?: number;
        };
        /** Represents settings for individual slices within each pie. */
        slice?: {
            /** Style to active slices in background (back navigation) pies */
            backgroundActiveStyle?: {
                brightness?: number;
            };
            /** Slice style on background (back navigation) pies. */
            backgroundStyle?: {
                fillColor?: string;
                /** Extra property to alternate fill colors on backgrond slices. */
                fillColor2?: string;
            };
            /** Label connector line style. */
            connectorStyle?: BaseSettingsLineStyle;
            /** Expandable slice mark line style */
            expandableMarkStyle?: {
                distance?: number;
                lineColor?: string;
                lineDash?: Array<number>;
                lineWidth?: number;
            };
            /** Hovered slices style. */
            hoverStyle?: PieChartSettingsSliceStyle;
            /** Slice margin. */
            margin?: number;
            /** Controls minimal visual size of slice. Use this to make very small sizes visually bigger. The value represents 
            smallest fraction of a full pie a slice will take. The range is between 0 and 1. For example using 0.05, all slices
            smaller than 5% will be grown up to 5%. Other slices will be made proportionally smaller to accommodate for extra 
            size of small slices. */
            minFraction?: number;
            /** Style for "Others" slice. */
            othersStyle?: {
                fillColor?: string;
                /** Circumference decoration of 'others' slice. */
                lineDecoration?: string;
            };
            /** Style for the "Previous" slice. */
            previousStyle?: {
                fillColor?: string;
                /** Circumference decoration of 'previous' slice. */
                lineDecoration?: string;
            };
            /** Selected slices style. */
            selectedStyle?: PieChartSettingsSliceStyle;
            /** Slice style settings */
            style?: PieChartSettingsSliceStyle;
            /** Dynamically determine slice style from data. */
            styleFunction?: (
                /** slice to apply predefined style */
                slice: PieChartSlice, 
                /** slice data including subvalues */
                sliceData: PieChartDataObject) => void;
        };
        /** Theme to apply. You can either use this to share configuration objects between multiple charts or use one of the predefined
        themes. */
        theme?: PieChartSettings;
        /** Adjustable settings to manage default and custom toolbar items, as well as toolbar overall appearance. */
        toolbar?: BaseSettingsToolbar;
    }
    export interface PieChartSettingsAdvanced extends BaseSettingsAdvanced {
        /** Controls visibility of the back icon at the center of pie chart. If true, the back button is always visible, otherwise only on hover. */
        backAlwaysVisible?: boolean;
        /** Image to display for back navigation. */
        backImage?: string;
        /** 
        @deprecated use the first value of `icons.sizeExtent` setting instead. */
        iconMinSize?: number;
        /** Enables/disables initial zoom-in animation. */
        initialAnimation?: boolean;
        /** Inside labels are not rendered if there is not enough space. So if available space is less than label size * treshold. */
        labelInsideTreshold?: number;
        /** Chart rendering quality. Note that it affects render performance for raised and gradient themes. Range 0 .. 1. */
        renderQuality?: number;
    }
    export interface PieChartSettingsData extends BaseSettingsData {
        /** An array of fields for auto-categorization. For each field a new drilldown sub-level will be created, separated by the values of that field. */
        autoCategories?: Array<(string|((obj: PieChartDataObject) => string))>;
        /** Load more chart data. */
        dataFunction?: (
            /** pie id */
            id: string, 
            /** max number of slices to load on pie */
            limit: number, 
            /** number of slices to skip from start */
            offset: number, 
            /** callback function to execute when data arrived correctly */
            success: (data: PieChartDataObjectRoot) => void, 
            /** callback function to execute when error occure while loading data */
            fail: (result: BaseDataErrorResponse) => void) => void;
        /** Number of slices to request from server. */
        itemsToLoad?: number;
        /** Enables/Disables partial load. Items will be loaded on demand but is not compatible with sortField or autoCategories. */
        partialLoad?: boolean;
        /** Provides the ability to embed chart data directly into the chart configuration.
        Data can be represented by an JavaScript object or a JSON string. */
        preloaded?: (string|PieChartDataObjectRoot);
        /** If set, items will be sorted in descending order using values from this field. */
        sortField?: string;
    }
    export interface PieChartSettingsEvents extends BaseSettingsEvents<PieChartChartEventArguments, PieChartChartClickEventArguments> {
        /** 
        @deprecated Use onChartUpdate instead */
        onPieChange?: (
            /** The mouse event. */
            event: MouseEvent, args: PieChartChartEventArguments) => void;
        /** 
        @deprecated Use onChartUpdate instead */
        onPieReadyStateChanged?: (
            /** The mouse event. */
            event: MouseEvent, args: PieChartChartEventArguments) => void;
    }
    export interface PieChartSettingsInteraction extends BaseSettingsInteraction {
        /** Chart animation settings. */
        animation?: {
            /** Duration of hover animation. */
            hoverDuration?: number;
            /** Duration of scroll animation. */
            scrollDuration?: number;
        };
        /** If true, empty donut center area reacts to clicks. */
        coverCenter?: boolean;
        /** Interaction mode. Note that `selection.enabled` can be used to disable the selection completely. */
        mode?: string;
        /** Slice including all the values that can not be displayed as a separate slice. Use them to configure 'others' slice rendering space. */
        others?: {
            /** If true, when offset > 0 click on center acts as click on previous slice. */
            centerGoesToPrevious?: boolean;
            /** Enable/Disable Others/Previous slices. */
            enabled?: boolean;
            /** Max size of others slice, as a fraction of 1. If others slice is bigger than this, it will be made smaller and other slices proportionally expanded. */
            maxOthersFraction?: number;
            /** Max number of slices to show. All remaining slices will be replaced by 'Others' slice. */
            maxSlicesVisible?: number;
            /** Minimum slice size as fraction of full pie. All slices smaller than that are replaced with 'Others' slice. */
            minSliceFraction?: number;
            /** Others and Previous slice size as fraction of full pie.
            Previous will always be this big.
            Other will be at least this big but no bigger than maxOthersFraction. */
            navigationFraction?: number;
        };
        /** One of interactivity option to scroll among data. */
        scrolling?: {
            /** Enable/disable scrolling the Other/Previous slices by rotating the chart. */
            enabled?: boolean;
        };
        /** One of interactivity option to use the data selection. */
        selection?: {
            /** Enable/disable selection. */
            enabled?: boolean;
            /** Max cutout distance on swipe. */
            maxSwipeDistance?: number;
            /** Allows partial selecting of slices by gradually dragging them outwards. If this is set to `true`, the slices will not snap to true/false positions 
            instead the API will provide information on how far they were pulled out. */
            partialSwipe?: boolean;
            /** Wether to select slice by swipe. Note that it works independently of selection mode. */
            swipe?: boolean;
            /** Pixels the pointer has to be moved before the motion is recognized as a swipe. */
            swipeSensitivity?: number;
            /** Selection distance tolerance. */
            tolerance?: number;
        };
    }
    export interface PieChartSettingsLabels {
        /** Label rotation angle. */
        angle?: number;
        /** Minimal connector length from slice to label. */
        connectorLength?: number;
        /** Whether to show connector lines for labels. */
        connectors?: boolean;
        /** Show/hide labels. */
        enabled?: boolean;
        /** Inside label placement method. */
        insideLabel?: string;
        /** Inside label is not shown if the fraction of label that fits inside slice is smaller than this. */
        insideLabelVisibilityFraction?: number;
        /** Min distance between labels, as a fraction of line height. */
        interLabelSpacing?: number;
        /** Outside labels placement method. */
        placement?: string;
    }
    export interface PieChartSettingsLegend extends BaseSettingsLegend {
        /** Visual element of legend entry with appropriate style to a slice color it corresponds. 
        The content of each legend marker is the same as info popup appearing while hovering on slice. */
        marker?: PieChartSettingsLegendMarker;
    }
    export interface PieChartSettingsLegendMarker extends BaseSettingsLegendMarker {
        /** Specifies the shape for color markers in the legend. */
        shape?: string;
    }
    export interface PieChartSettingsLocalization extends BaseSettingsLocalization {
        othersLabel?: string;
        previousLabel?: string;
    }
    export interface PieChartSettingsPieStyle {
        background?: boolean;
        /** Pie background brightness. */
        brightness?: number;
        /** Color distribution among slices. */
        colorDistribution?: string;
        /** Pie background fill color. Useful while pie slices are loading. */
        fillColor?: string;
        /** Color set for pie slices. */
        sliceColors?: Array<string>;
    }
    export interface PieChartSettingsSliceStyle {
        /** Brightness applied to slice line and fill color
        value 0 - black
        value 1 - unchanged
        value 2 - transparent */
        brightness?: number;
        /** Distance how far the slice is moved away from pie. */
        cutoutDistance?: number;
        /** Specifies if the slice is expandable. */
        expandable?: boolean;
        /** Slice fill color. */
        fillColor?: string;
        /** Icon to display on slice. */
        icon?: string;
        /** Gets or sets the style of the label shown inside the slice. Use `insideLabel.text` to specify the text that will be displayed. */
        insideLabel?: BaseSettingsLabelStyle;
        /** Gets or sets the style of the external label. Use `label.text` to specify the text that will be displayed. */
        label?: BaseSettingsLabelStyle;
        /** Brightness applied to slice line color */
        lineBrightness?: number;
        /** Outline color. */
        lineColor?: string;
        /** Line dash array that is a pattern to get a dashed line. The array contains length of dash followed by length of space. 
        Note that a sequence of multiple dash-space values is supported. */
        lineDash?: Array<number>;
        /** Width of the slice outline. */
        lineWidth?: number;
        /** Url to open on click. */
        url?: string;
    }
    export interface PieChartSlice {
        active: boolean;
        brightness: number;
        cutoutDistance: number;
        data: PieChartDataObject;
        /** Whether to expand the slice as a default click behavior. */
        expandable: boolean;
        fillColor: string;
        fillColor2: string;
        fraction: number;
        icon: string;
        iconOffset: [number, number, number, number];
        id: string;
        index: number;
        inside: boolean;
        /** Gets or sets the style of the label shown inside the slice. Use `insideLabel.text` to specify the text that will be displayed.
        Note that for backwards compatibility it is possible to set this property to a string directly - in this case the value will be
        written the `insideLabel.text` but also `label.text` will be cleared. */
        insideLabel: BaseSettingsLabelStyle;
        /** 
        @deprecated use `insideLabel` instead. */
        insideLabelStyle: BaseSettingsLabelStyle;
        /** Gets or sets the style of the external label. Use `label.text` to specify the text that will be displayed. */
        label: BaseSettingsLabelStyle;
        /** 
        @deprecated use `label` instead. */
        labelStyle: BaseSettingsLabelStyle;
        lineBrightness: number;
        lineColor: string;
        lineDash: Array<number>;
        lineDecoration: string;
        lineWidth: number;
        percent: number;
        pie: PieChartPie;
        removed: boolean;
        /** Swipe distance towards the center of the pie */
        selectDistance: number;
        selected: boolean;
        /** Slice partial swipe distance */
        selection: number;
        url: string;
        userPlaced: boolean;
        value: number;
        x: number;
        y: number;
    }
    /** Describes the base properties shared between all events raised by the different charts. */
    export interface TimeChartChartEventArguments extends BaseChartEventArguments {
        /** 
        @deprecated use hoverEnd instead */
        clickEnd: number;
        /** 
        @deprecated use hoverMarker instead */
        clickMarker: boolean;
        /** 
        @deprecated use hoverSeries instead */
        clickSeries: TimeChartSettingsSeries;
        /** 
        @deprecated use hoverStart instead */
        clickStart: number;
        displayUnit: string;
        hoverEnd: number;
        hoverMarker: boolean;
        hoverSeries: TimeChartSettingsSeries;
        hoverStart: number;
        selectionEnd: number;
        selectionStart: number;
        timeEnd: number;
        timeStart: number;
    }
    export interface TimeChartDataObject extends BaseDataErrorResponse {
        /** 
        @deprecated Use `values` instead. */
        data?: Array<Array<number>>;
        dataLimitFrom?: number;
        dataLimitTo?: number;
        from: number;
        to: number;
        unit: string;
        /** The list of values for the chart. Each array represents values for a single point in time. The first (index 0) value must
        contain the timestamp value, the rest are values for individual series.
        For example: [[time1, series1_Val1, series2_Val1], [time2, series1_Val2, series2_Val2]] */
        values: Array<Array<number>>;
    }
    export interface TimeChartSettings extends LinearChartSettings {
        advanced?: TimeChartSettingsAdvanced;
        /** Chart area related settings. */
        area?: TimeChartSettingsArea;
        /** Default series settings for each series rendering type. Use this to configure all series of specific type to get line
        or column chart or combination of them. */
        chartTypes?: {
            /** Series type to show an opening and closing value on top of a total variance. */
            candlestick?: TimeChartSettingsSeriesCandleStick;
            /** Series type to render values as vertical bars. */
            columns?: TimeChartSettingsSeriesColumns;
            /** Series type to connect value points by lines. */
            line?: TimeChartSettingsSeriesLines;
        };
        /** Moving time line that represents the current time, mainly to analyze real-time data changes. To achieve it,
        use server time of page load and adjust time zone offset. */
        currentTime?: TimeChartSettingsCurrentTime;
        /** Settings used to load data into chart. Customise preferred data source feeding methods.
        You can use one of these options: url, data function, preloaded data. */
        data?: Array<TimeChartSettingsData>;
        /** The events used to handle user interaction with UI elements. */
        events?: TimeChartSettingsEvents;
        /** A variety of interaction options that includes scrolling, zooming and swipe. */
        interaction?: TimeChartSettingsInteraction;
        /** Localizeable strings including export type options and useful default buttons used for chart interaction.
        Buttons like to navigate back, set the chart on full screen and others. */
        localization?: TimeChartSettingsLocalization;
        /** Time line markers that represents event on time. */
        milestones?: Array<TimeChartSettingsMarker>;
        /** Settings to specify initial view once the page loaded. */
        navigation?: {
            /** Whether to allow the chart view to follow display anchor. Use it for real-time updates. */
            followAnchor?: boolean;
            /** Specifies anchor position for initial time period. Use it together with initialDisplayPeriod from which beginning is calculated. */
            initialDisplayAnchor?: string;
            /** Time period to show initially. Use in combination with intitialDisplayAnchor.
            Range displayed on time axis according to local or specified time-zone offset. */
            initialDisplayPeriod?: string;
            /** Data display unit to use for initial view. */
            initialDisplayUnit?: string;
        };
        /** Array of series in the chart. Each of the series can be different type, can use different data source and
        aggregation. Additionally, series can be clustered and stacked. */
        series?: Array<TimeChartSettingsSeries>;
        /** The default series used as the chart dominant data. Use settings.series array to specify actual series. */
        seriesDefault?: TimeChartSettingsSeries;
        /** Theme to apply. You can either use this to share configuration objects between multiple charts or use one of the predefined
        themes. */
        theme?: TimeChartSettings;
        /** X-axis line representing time at the bottom of chart.
        Time line made of two different time graduations. Next image shows time line graduation by days and 3 hours.
        ![time axis](time-chart/images/settings-timeAxis.png)
        As you change the zoom level time axis graduation changes accordingly. */
        timeAxis?: TimeChartSettingsTimeAxis;
        /** Adjustable settings to manage default and custom toolbar items, as well as toolbar overall appearance. */
        toolbar?: TimeChartSettingsToolbar;
    }
    export interface TimeChartSettingsAdvanced extends BaseSettingsAdvanced {
        /** Determines how frequently (in ms) to poll for new data. If set to null data polling is disabled.
        The data is automatically requested for the period since the last available timestamp until the current time.
        Note that if there is no data available yet for the time period the chart requests, an empty data response should be
        returned, for example `success({ from: 0, to: 1, unit: "ms", values: [] })`. */
        dataUpdateInterval?: number;
        /** Maximum data points to render in one view. Note that TimeChart will not allow to select such time unit that will result in more points being rendered. */
        maxUnitsToDisplay?: number;
        /** The minimum time interval in which current time marker is updated, in ms. Normally the value is calculated based on the time scale
        so that the time only updates when the time marker would be moved and this value is only used as a baseline. */
        timeUpdateInterval?: number;
    }
    export interface TimeChartSettingsArea extends LinearChartSettingsArea {
        /** List of time periods that are used when formatting time axis. */
        displayPeriods?: Array<{
                unit?: string;
            }>;
        /** List of time units to use for data aggregation. */
        displayUnits?: Array<{
                /** Used units */
                unit?: string;
                /** Name of used units */
                name?: string;
            }>;
        /** Area style settings. */
        style?: TimeChartSettingsAreaStyle;
    }
    export interface TimeChartSettingsAreaDisplayPeriod {
        /** Reference time for passed data. */
        displayAnchor?: string;
        /** Display period. */
        displayPeriod?: string;
        /** Units in which to show data. */
        displayUnit?: string;
        /** Named time interval. */
        name?: string;
    }
    export interface TimeChartSettingsAreaStyle extends LinearChartSettingsAreaStyle {
        /** Style for the spinning 'Loading data' indicator */
        loadingData?: {
            fillColor?: string;
        };
        /** Text style for all markers. */
        markerText?: BaseSettingsTextStyle;
        /** Time period selection style. */
        selection?: {
            fillColor?: string;
            lineColor?: string;
            lineWidth?: number;
        };
        /** Time period selection text style. */
        selectionLabel?: BaseSettingsTextStyle;
    }
    export interface TimeChartSettingsCurrentTime extends TimeChartSettingsMarker {
        /** Style settings for the drawing the time line text on the chart. You can also use `label.text` property
        to specify additional text that will be shown next to the time or by setting `showTime` to `false` (the default)
        display only the specified text. */
        label?: BaseSettingsLabelStyle;
        /** Show the milestone time along with the label text */
        showTime?: boolean;
        /** Align label relative to chart top or bottom side */
        side?: string;
        /** The style for the vertical time marker line. */
        style?: BaseSettingsBackgroundStyle;
    }
    export interface TimeChartSettingsData extends BaseSettingsData {
        /** Maximum number of data entries to store per data unit. */
        cacheSize?: number;
        /** Load more chart data. */
        dataFunction?: (
            /** timestamp from which data to be loaded */
            from: number, 
            /** timestamp from which data to be loaded */
            to: number, 
            /** time steps at what data can be fetched */
            unit: string, 
            /** callback function to execute when data arrived correctly. The value can be an object or JSON string. */
            success: (data: (TimeChartDataObject|string)) => void, 
            /** callback function to execute when error occure while loading data. The value can be an object or JSON string. */
            fail: (result: (BaseDataErrorResponse|string)) => void) => void;
        /** Whether to request only when scrolling/scaling has stopped. */
        minimizeRequests?: boolean;
        /** How much data to load in advance. For example, ratio * chart width worth of data is loaded in both directions. */
        prefetchRatio?: number;
        /** Provides the ability to embed chart data directly into the chart configuration.
        Data can be represented by an JavaScript object or a JSON string. */
        preloaded?: TimeChartDataObject;
        /** Maximum number of data points to request in one go. Multiple requests will be issued if more data is needed. */
        requestMaxUnits?: number;
        /** Data time zone offset in minutes. */
        timeZoneOffset?: (string|number);
        /** Whether to use timestamp in seconds instead of milliseconds. */
        timestampInSeconds?: boolean;
        /** Time steps at what data can be fetched. */
        units?: Array<string>;
        /** Different urls by data units. Specify specific URL for each time unit. For example, specify data file where
        time aggregated by years - urlByUnit:{ 'y':'/data/yearData.json} 
        If this is specified, it overrides `url` property. */
        urlByUnit?: {
            /** URL for data for each month */
            M?: string;
            /** URL for data for each day */
            d?: string;
            /** URL for data for each hour */
            h?: string;
            /** URL for data for each minute */
            m?: string;
            /** URL for data for each millisecond */
            ms?: string;
            /** URL for data for each second */
            s?: string;
            /** URL for data for each year */
            y?: string;
        };
    }
    export interface TimeChartSettingsEvents extends LinearChartSettingsEvents<TimeChartChartEventArguments, TimeChartChartEventArguments> {
        /** 
        @deprecated same as onChartUpdate */
        onTimeChange?: (
            /** An empty mouse event. */
            event: BaseMouseEvent, args: TimeChartChartEventArguments) => void;
    }
    export interface TimeChartSettingsInteraction extends LinearChartSettingsInteraction {
        /** Scrolling settings. */
        scrolling?: TimeChartSettingsInteractionScrolling;
        /** Configurable settings to select specific time period. */
        selection?: TimeChartSettingsInteractionSelection;
        /** Snap mode determines how the chart locks on to data periods after dragging, scrolling and other actions. */
        snapMode?: string;
    }
    export interface TimeChartSettingsInteractionScrolling extends LinearChartSettingsInteractionScrolling {
        /** Limits scrolling in the left side. This should be either the timestamp value in milliseconds or the string constant 'oldestData'.
        Specify `null` to disable the limit. */
        limitFrom?: (string|number);
        /** Defines the chart behavior when the chart is scrolled passed the data limits. The actual limits are stored in `limitFrom` and `limitTo`. */
        limitMode?: string;
        /** Limits scrolling in the right side. This should be either the timestamp value in milliseconds or the string constant 'newestData'.
        Specify `null` to disable the limit. */
        limitTo?: (string|number);
        /** Allows the user to scroll past the limit to a certain degree. This coefficient is multiplied to the overscroll area - so if `overscrollProportion`
        is set to 0.1 and the user scrolls 10 units past the limit, the chart will scroll back 9 units. */
        overscrollProportion?: number;
    }
    export interface TimeChartSettingsInteractionSelection {
        /** Enable/disable selection */
        enabled?: boolean;
        /** Distance in pixels to both sides of the selection left/right edges that can be used to drag the selection area to expand/collapse it. */
        tolerance?: number;
    }
    export interface TimeChartSettingsLocalization extends LinearChartSettingsLocalization {
        /** Calendar specific localization settings. This section is used to initialize moment.js locale.
        See moment.js documentation for detailed description: http://momentjs.com/docs/#/customization/ . 
        
        Please note that currently it is not supported for different charts on the same page to have different localization settings.
        The settings from the chart that was initialized last will be used always. */
        calendar?: {
            months?: Array<string>;
            monthsShort?: Array<string>;
            week?: {
                /** Specifies the first day of the week. The default is `1` - Monday. Use either `0` or `7` for Sunday. */
                dow?: number;
                /** Specifies how the first week of the year is calculated. The default is `4` - the week that contains Jan 4th is counted as the first. 
                
                Note that this setting is not used by default because the default formatting specifies ISO week numbering. If you want to change that,
                use `w` and `gggg` when specifying the week format strings. */
                doy?: number;
            };
            weekdays?: Array<string>;
            weekdaysMin?: Array<string>;
            weekdaysShort?: Array<string>;
        };
        /** Message being displayed while loading initial data. */
        determiningDataBounds?: string;
        /** Weekdays that are considered holidays. Uses ISO numbering, with 1 being Monday and 7 being Sunday. */
        holidayWeekdays?: Array<number>;
        /** Localizable strings displayed in info popup. */
        infoDates?: {
            /** Time formatting strings to display full time for given time unit. */
            fullTimeFormats?: TimeChartSettingsLocalizationTimeUnits;
            /** Time formatting strings for time marker date. */
            majorTimeFormats?: TimeChartSettingsLocalizationTimeUnits;
            /** Time formatting strings for info popup time display. */
            minorTimeFormats?: TimeChartSettingsLocalizationTimeUnits;
        };
        /** Message to show when data loading is in progress. */
        loadingLabel?: string;
        /** Localizable strings for markers. */
        markerDates?: {
            /** Time formatting strings for time marker date. */
            timeFormats?: TimeChartSettingsLocalizationTimeUnits;
        };
        /** Localizable strings displayed in time axis. */
        timeAxisDates?: {
            /** Time formatting strings for major time units in bottom line of time axis. */
            majorLabelFullTimeFormats?: TimeChartSettingsLocalizationTimeUnits;
            /** Time formatting strings for major time units in bottom line of time axis. */
            majorLabelTimeFormats?: TimeChartSettingsLocalizationTimeUnits;
            /** Time formatting strings for minor time units in top line of time axis. */
            minorLabelTimeFormats?: TimeChartSettingsLocalizationTimeUnits;
        };
        /** Named time units. */
        timeUnitsNames?: TimeChartSettingsLocalizationTimeUnits;
        /** Plural forms of time units. */
        timeUnitsNamesPlural?: TimeChartSettingsLocalizationTimeUnits;
        /** Strings used in toolbars. */
        toolbar?: TimeChartSettingsLocalizationToolbar;
    }
    export interface TimeChartSettingsLocalizationTimeUnits {
        /** Months */
        M?: string;
        d?: string;
        h?: string;
        /** Minutes */
        m?: string;
        /** Milliseconds */
        ms?: string;
        s?: string;
        w?: string;
        y?: string;
    }
    export interface TimeChartSettingsLocalizationToolbar extends LinearChartSettingsLocalizationToolbar {
        /** Text to show in display periods drop-down when current period does not match any of the presets. */
        customPeriod?: string;
        /** Display period dropdown title. */
        periodDropdownTitle?: string;
        /** Display unit dropdown title. */
        unitDropdownTitle?: string;
        /** Zoom out button text. */
        zoomoutButton?: string;
        /** Zoom out button title. */
        zoomoutTitle?: string;
    }
    export interface TimeChartSettingsMarker {
        /** Align label relative to the line */
        align?: string;
        /** Show/hide the marker. */
        enabled?: boolean;
        /** Style settings for the drawing the time line text on the chart. You can also use `label.text` property
        to specify additional text that will be shown next to the time or by setting `showTime` to `false` (the default)
        display only the specified text. */
        label?: BaseSettingsLabelStyle;
        /** Specifies if the time marker is always included when limiting scrolling. If this is `true`, then the user will always
        be able to scroll to the current time even if the data is not available. */
        overrideLimits?: boolean;
        /** Show the milestone time along with the label text */
        showTime?: boolean;
        /** Align label relative to chart top or bottom side */
        side?: string;
        /** The style for the vertical time marker line. */
        style?: BaseSettingsLineStyle;
        /** Timestamp in UTC milliseconds to locate time marker on chart */
        time?: number;
    }
    export interface TimeChartSettingsSeries extends LinearChartSettingsSeries {
        /** Data manipulation settings used for default series. */
        data?: TimeChartSettingsSeriesData;
    }
    export interface TimeChartSettingsSeriesCandleStick extends LinearChartSettingsSeriesCandleStick {
        /** Data manipulation settings used for default series. */
        data?: TimeChartSettingsSeriesCandleStickData;
    }
    export interface TimeChartSettingsSeriesCandleStickData extends TimeChartSettingsSeriesData {
        /** The index in the value arrays where the closing values in that particular time period is located. */
        close?: number;
        /** The index in the value arrays where the highest values in that particular time period is located. */
        high?: number;
        /** The index in the value arrays where the lowest values in that particular time period is located. */
        low?: number;
        /** The index in the value arrays where the opening values in that particular time period is located. */
        open?: number;
    }
    export interface TimeChartSettingsSeriesColumns extends LinearChartSettingsSeriesColumns {
        /** Data manipulation settings used for default series. */
        data?: TimeChartSettingsSeriesData;
    }
    export interface TimeChartSettingsSeriesData extends LinearChartSettingsSeriesData {
        /** Index in the value array where the count of aggregated values are located. This is optional but should be provided if the aggregation
        method is `avg` (average) and URL or `dataFunction` is used to aggregate the values external to the chart because otherwise the chart will 
        aggregate values by simply counting them. So if `countIndex` is not provided and there are values:
        `[[May 1 2014, 5], [May 2 2014, 15], [June 1 2014, 20]]` the aggregation to year 2014 will yield `(5+15+20)/3 = 13.3` when the display unit is
        days but `((5+15)/2 + 20)/2 = 15` when the display unit is months. */
        countIndex?: number;
        /** Index in value the array where the values that will be aggregated are located. An alternative is to use `valueFunction`. */
        index?: number;
    }
    export interface TimeChartSettingsSeriesLines extends LinearChartSettingsSeriesLines {
        /** Data manipulation settings used for default series. */
        data?: TimeChartSettingsSeriesData;
    }
    export interface TimeChartSettingsTimeAxis {
        /** Shows/hides time axis. */
        enabled?: boolean;
        /** Maximal width of displayed cart value, in px. If the width is bigger than this, 
        Time Chart will use a smaller time unit (e.g. switch from minutes to seconds). */
        maxUnitWidth?: number;
        /** Minimum width of a displayed value, in px . If the width is smaller than this, Time Chart will switch to bigger time units (e.g. from minutes to hours). */
        minUnitWidth?: number;
        /** Whether to show smallest bars on time axis. It matches with display unit dropdown entry used in time chart toolbar. */
        miniTimeRuler?: boolean;
        /** Whether to show holidays in day view. */
        showHolidays?: boolean;
        /** Time axis style */
        style?: {
            dateHolidays?: {
                fillColor?: string;
            };
            /** Tint for lighter blocks of altering shades in time axis. */
            dateLighten?: {
                fillColor?: string;
            };
            /** Style for major time labels balloons. */
            majorTimeBalloonStyle?: BaseSettingsBackgroundStyle;
            /** Style for major time labels. */
            majorTimeLabel?: BaseSettingsTextStyle;
            /** Style for minor time labels balloons. */
            minorTimeBalloonStyle?: BaseSettingsBackgroundStyle;
            /** Style for minor time labels. */
            minorTimeLabel?: BaseSettingsTextStyle;
            /** Minor time ruler style. */
            minorTimeRuler?: BaseSettingsLineStyle;
            /** Whether to show balloons around major time labels. */
            showMajorTimeBalloons?: boolean;
            /** Whether to display minor time labels as balloons instead of ruler style. */
            showMinorTimeBalloons?: boolean;
            /** Chart vertical grid settings */
            vgrid?: BaseSettingsLineStyle;
        };
        /** Time axis time zone offset in minutes. */
        timeZoneOffset?: (string|number);
        /** Display unit switching policy. */
        unitSizePolicy?: string;
        /** Whether to show vertical grid. */
        vgrid?: boolean;
    }
    export interface TimeChartSettingsToolbar extends BaseSettingsToolbar {
        /** Whether to show display period dropdown. */
        displayPeriod?: boolean;
        /** Whether to show the display unit dropdown. */
        displayUnit?: boolean;
        /** Show/hide toolbar. */
        enabled?: boolean;
        /** A list of toolbar items. Use it to completely override the items in toolbar. */
        items?: Array<BaseSettingsToolbarItem>;
        /** Whether to show the Lin/Log button in the toolbar. */
        logScale?: boolean;
        /** Time periods */
        periods?: Array<TimeChartSettingsAreaDisplayPeriod>;
        /** Whether to show the zoom out button. */
        zoomOut?: boolean;
    }
    /* tslint:enable */
}

declare module ZoomCharts {
    /* tslint:disable */

    export class FacetChart {
        public constructor(settings: Configuration.FacetChartSettings);
        /** Adds the given data to whatever data the chart has currently loaded. The chart will automatically be updated
        to display this new data if it falls within the currently visible bounds. */
        public addData(data: Configuration.PieChartDataObjectRoot, sourceId?: string): void;
        public expandSlice(slice: Configuration.FacetChartItem): boolean;
        public getActiveFacet(): Configuration.FacetChartFacet;
        public getActiveFacets(): Array<Configuration.FacetChartFacet>;
        public getActiveItems(): Array<Configuration.FacetChartItem>;
        public getPie(): Array<string>;
        public getPieOffset(): number;
        /** Adds event listener. */
        public on(
            /** The type of the event for which the listener will be added. See method overloads for valid values. */
            name: string, 
            /** The callback function. It receives two arguments - the mouse event data and a separate object containing chart specific information. */
            listener: (event: Configuration.BaseMouseEvent, args: Configuration.BaseChartEventArguments) => void): void;
        /** Adds an event listener for when the scroll animation completes. */
        public on(name: "animationDone", listener: (event: Configuration.BaseMouseEvent, args: Configuration.FacetChartChartEventArguments) => void): void;
        public on(name: "chartUpdate", listener: (event: Configuration.BaseMouseEvent, args: Configuration.FacetChartChartEventArguments) => void): void;
        public on(name: "click", listener: (event: Configuration.BaseMouseEvent, args: Configuration.FacetChartChartClickEventArguments) => void): void;
        public on(name: "doubleClick", listener: (event: Configuration.BaseMouseEvent, args: Configuration.FacetChartChartClickEventArguments) => void): void;
        public on(name: "error", listener: (
                /** An empty mouse event. */
                event: Configuration.BaseMouseEvent, args: Configuration.BaseChartErrorEventArguments) => void): void;
        public on(name: "hoverChange", listener: (event: Configuration.BaseMouseEvent, args: Configuration.FacetChartChartEventArguments) => void): void;
        public on(name: "positionChange", listener: (event: Configuration.BaseMouseEvent, args: Configuration.FacetChartChartEventArguments) => void): void;
        public on(name: "rightClick", listener: (event: Configuration.BaseMouseEvent, args: Configuration.FacetChartChartClickEventArguments) => void): void;
        public on(name: "selectionChange", listener: (event: Configuration.BaseMouseEvent, args: Configuration.FacetChartChartEventArguments) => void): void;
        public on(name: "settingsChange", listener: (event: Configuration.BaseMouseEvent, args: Configuration.BaseChartSettingsChangeEventArguments) => void): void;
        public on(name: "tripleClick", listener: (event: Configuration.BaseMouseEvent, args: Configuration.FacetChartChartClickEventArguments) => void): void;
        public removeData(data: Configuration.PieChartDataObjectRoot, sourceId?: string): void;
        public replaceData(data: Configuration.PieChartDataObjectRoot, sourceId?: string): void;
        /** Replaces the existing series configuration with new one instead of merging it like `updateSettings`.
        @deprecated Use `replaceSettings({series: seriesData})` instead. */
        public replaceSeries(series: Array<Configuration.LinearChartSettingsSeries>): FacetChart;
        /** Updates the chart settings but instead of merging some settings that are arrays or dictionaries (such as `data`)
        these collections are replaced completely. For example, this allows removal of series or value axis within TimeChart. */
        public replaceSettings(changes: Configuration.FacetChartSettings): FacetChart;
        public selection(selected: Array<(Configuration.FacetChartItem|string)>): Array<Configuration.FacetChartItem>;
        public setPie(pieId: (string|Array<string>), offset?: number, count?: number): FacetChart;
        /** Lists the predefined themes for the chart. These can be used within the settings objects or via the `customize()` method:
        
        ```javascript 
        var chart = new ZoomCharts.$this({ theme: ZoomCharts.$this.dark });
        chart.updateSettings({ theme: ZoomChart.$this.dark });
        chart.customize("dark");
        ``` */
        public static themes: {
            dark?: Configuration.FacetChartSettings;
        };
        public updateFilter(): FacetChart;
        /** Updates the chart settings. Only the settings that have to be changed should be passed. Note that some arrays
        and dictionaries (such as `data`) are merged by the ID values - if instead they should be replaced, use
        [`replaceSettings()`](#doc_replaceSettings) method. */
        public updateSettings(changes: Configuration.FacetChartSettings): FacetChart;
    }
    export class GeoChart {
        public constructor(settings: Configuration.GeoChartSettings);
        /** Adds the given data to whatever data the chart has currently loaded. The chart will automatically be updated
        to display this new data if it falls within the currently visible bounds. */
        public addData(data: Configuration.GeoChartDataObject, sourceId?: string): void;
        public back(): GeoChart;
        public bounds(bounds?: IGeoRectangle): IGeoRectangle;
        public getNode(id: string): Configuration.ItemsChartNode;
        public getNodeDimensions(node: (Configuration.ItemsChartNode|string)): {
                x: number;
                y: number;
                radius: number;
                hwidth: number;
            };
        public hideMenu(): GeoChart;
        /** Returns the Leaflet.Map object to enable any advanced customizations.
        Please note that this method might return `null` if `leaflet.js` has not been loaded yet. 
        If `leaflet.js` is not loaded synchronously in the page, it will be loaded on demand by GeoChart. 
        In this case the map object will not be available until the load is finished.
        
        To handle this scenario, subscribe to `onChartUpdate` event and verify if this method returns non-null value. */
        public leaflet(): any;
        /** Adds event listener. */
        public on(
            /** The type of the event for which the listener will be added. See method overloads for valid values. */
            name: string, 
            /** The callback function. It receives two arguments - the mouse event data and a separate object containing chart specific information. */
            listener: (event: Configuration.BaseMouseEvent, args: Configuration.BaseChartEventArguments) => void): void;
        public on(name: "chartUpdate", listener: (event: Configuration.BaseMouseEvent, args: Configuration.ItemsChartChartEventArguments) => void): void;
        public on(name: "click", listener: (event: Configuration.BaseMouseEvent, args: Configuration.ItemsChartChartClickEventArguments) => void): void;
        public on(name: "doubleClick", listener: (event: Configuration.BaseMouseEvent, args: Configuration.ItemsChartChartClickEventArguments) => void): void;
        public on(name: "error", listener: (
                /** An empty mouse event. */
                event: Configuration.BaseMouseEvent, args: Configuration.BaseChartErrorEventArguments) => void): void;
        public on(name: "hoverChange", listener: (event: Configuration.BaseMouseEvent, args: Configuration.ItemsChartChartEventArguments) => void): void;
        public on(name: "positionChange", listener: (event: Configuration.BaseMouseEvent, args: Configuration.ItemsChartChartEventArguments) => void): void;
        public on(name: "rightClick", listener: (event: Configuration.BaseMouseEvent, args: Configuration.ItemsChartChartClickEventArguments) => void): void;
        public on(name: "selectionChange", listener: (event: Configuration.BaseMouseEvent, args: Configuration.ItemsChartChartEventArguments) => void): void;
        public on(name: "settingsChange", listener: (event: Configuration.BaseMouseEvent, args: Configuration.BaseChartSettingsChangeEventArguments) => void): void;
        public on(name: "tripleClick", listener: (event: Configuration.BaseMouseEvent, args: Configuration.ItemsChartChartClickEventArguments) => void): void;
        public removeData(data: Configuration.GeoChartDataObject, sourceId?: string): void;
        public replaceData(data: Configuration.GeoChartDataObject, sourceId?: string): void;
        /** Updates the chart settings but instead of merging some settings that are arrays or dictionaries (such as `data`)
        these collections are replaced completely. For example, this allows removal of series or value axis within TimeChart. */
        public replaceSettings(changes: Configuration.GeoChartSettings): GeoChart;
        /** Set/Get selected objects. */
        public selection(
            /** array of objects identifiers to select. Do not pass this parameter if you don't want to change current selection. */
            selected: Array<(string|Configuration.ItemsChartNode|Configuration.ItemsChartLink)>): Array<(Configuration.ItemsChartNode|Configuration.ItemsChartLink)>;
        /** Lists the predefined themes for the chart. These can be used within the settings objects or via the `customize()` method:
        
        ```javascript 
        var chart = new ZoomCharts.$this({ theme: ZoomCharts.$this.dark });
        chart.updateSettings({ theme: ZoomChart.$this.dark });
        chart.customize("dark");
        ``` */
        public static themes: {
            flat?: Configuration.GeoChartSettings;
        };
        /** Updates the chart settings. Only the settings that have to be changed should be passed. Note that some arrays
        and dictionaries (such as `data`) are merged by the ID values - if instead they should be replaced, use
        [`replaceSettings()`](#doc_replaceSettings) method. */
        public updateSettings(changes: Configuration.GeoChartSettings): GeoChart;
        /** Updates (recalculates) the style for the whole chart or specific objects matching the given IDs. */
        public updateStyle(
            /** A list of IDs for the objects which need their style recalculated */
            objects?: Array<string>): void;
        public zoomLevel(newZoom?: number): number;
    }
    export class NetChart {
        public constructor(settings: Configuration.NetChartSettings);
        /** Adds the given data to whatever data the chart has currently loaded. The chart will automatically be updated
        to display this new data if it falls within the currently visible bounds. */
        public addData(data: Configuration.NetChartDataObject, sourceId?: string): void;
        /** Focuses a node. Whether or not the node will get actually focused depends no the navigation mode. */
        public addFocusNode(
            /** Node ID or object */
            id: (string|Configuration.ItemsChartNode), 
            /** Explicitly assigned relevance (used only by Focusnodes navigation mode).
            For more information, see the [Focusnodes algorithm](net-chart/advanced-topics/focusnodes-algorithm-details.html) */
            relevance?: number): void;
        /** Removes focus from all nodes. The exact effect depends on the navigation mode. */
        public clearFocus(): void;
        /** Collapses a node. The exact effect depends on the navigation mode. */
        public collapseNode(
            /** Node ID or object */
            id: (string|Configuration.ItemsChartNode)): void;
        /** Expands a visible node. */
        public expandNode(
            /** Node ID or object */
            id: (string|Configuration.ItemsChartNode)): void;
        public exportData(visibleOnly?: boolean, exportCoordinates?: boolean): Configuration.NetChartDataObject;
        /** Gets a visible link by its ID */
        public getLink(
            /** Link ID */
            id: string): Configuration.ItemsChartLink;
        /** Gets a visible node by its ID */
        public getNode(
            /** Node ID */
            id: string): Configuration.ItemsChartNode;
        public getNodeDimensions(node: Configuration.ItemsChartNode): {
                x: number;
                y: number;
                radius: number;
                hwidth: number;
            };
        public hideMenu(): NetChart;
        /** Hides a visible node. Whether or not the node will get actually hidden depends on the navigation mode. */
        public hideNode(
            /** Node ID or object */
            id: (string|Configuration.ItemsChartNode)): void;
        public links(): Array<Configuration.ItemsChartLink>;
        /** Fixates a node in place. */
        public lockNode(
            /** Node ID or object */
            id: (string|Configuration.ItemsChartNode), x: number, 
            /** Y position, in scene coordinates */
            y: number): void;
        public nodes(): Array<Configuration.ItemsChartNode>;
        /** Adds event listener. */
        public on(
            /** The type of the event for which the listener will be added. See method overloads for valid values. */
            name: string, 
            /** The callback function. It receives two arguments - the mouse event data and a separate object containing chart specific information. */
            listener: (event: Configuration.BaseMouseEvent, args: Configuration.BaseChartEventArguments) => void): void;
        public on(name: "chartUpdate", listener: (event: Configuration.BaseMouseEvent, args: Configuration.NetChartChartEventArguments) => void): void;
        public on(name: "click", listener: (event: Configuration.BaseMouseEvent, args: Configuration.NetChartChartClickEventArguments) => void): void;
        public on(name: "doubleClick", listener: (event: Configuration.BaseMouseEvent, args: Configuration.NetChartChartClickEventArguments) => void): void;
        public on(name: "error", listener: (
                /** An empty mouse event. */
                event: Configuration.BaseMouseEvent, args: Configuration.BaseChartErrorEventArguments) => void): void;
        public on(name: "hoverChange", listener: (event: Configuration.BaseMouseEvent, args: Configuration.NetChartChartEventArguments) => void): void;
        public on(name: "positionChange", listener: (event: Configuration.BaseMouseEvent, args: Configuration.NetChartChartEventArguments) => void): void;
        public on(name: "rightClick", listener: (event: Configuration.BaseMouseEvent, args: Configuration.NetChartChartClickEventArguments) => void): void;
        public on(name: "selectionChange", listener: (event: Configuration.BaseMouseEvent, args: Configuration.NetChartChartEventArguments) => void): void;
        public on(name: "settingsChange", listener: (event: Configuration.BaseMouseEvent, args: Configuration.BaseChartSettingsChangeEventArguments) => void): void;
        public on(name: "tripleClick", listener: (event: Configuration.BaseMouseEvent, args: Configuration.NetChartChartClickEventArguments) => void): void;
        public removeData(data: Configuration.NetChartDataObject, sourceId?: string): void;
        /** Removes focus from a node. Whether or not the node will get actually unfocused depends on the navigation mode. */
        public removeFocusNode(
            /** Node ID or object */
            id: (string|Configuration.ItemsChartNode)): void;
        public replaceData(data: Configuration.NetChartDataObject, sourceId?: string): void;
        /** Updates the chart settings but instead of merging some settings that are arrays or dictionaries (such as `data`)
        these collections are replaced completely. For example, this allows removal of series or value axis within TimeChart. */
        public replaceSettings(changes: Configuration.NetChartSettings): NetChart;
        public resetLayout(): void;
        /** Animates the viewport to zoom into and contain the nodes specified in the given array */
        public scrollIntoView(nodes: (Array<string>|Array<Configuration.ItemsChartNode>)): void;
        /** Set/Get selected objects. */
        public selection(
            /** array of objects identifiers to select. Do not pass this parameter if you don't want to change current selection. */
            selected: Array<(string|Configuration.ItemsChartNode|Configuration.ItemsChartLink)>): Array<(Configuration.ItemsChartNode|Configuration.ItemsChartLink)>;
        /** Shows a node by its ID. The data for the node gets requested in the standard manner.
        Whether or not the node will get actually shown depends on the navigation mode. */
        public showNode(
            /** Node ID */
            id: string): void;
        /** Lists the predefined themes for the chart. These can be used within the settings objects or via the `customize()` method:
        
        ```javascript 
        var chart = new ZoomCharts.$this({ theme: ZoomCharts.$this.dark });
        chart.updateSettings({ theme: ZoomChart.$this.dark });
        chart.customize("dark");
        ``` */
        public static themes: {
            dark?: Configuration.NetChartSettings;
            flat?: Configuration.NetChartSettings;
        };
        /** Unfixates a node and allows it to be repositioned by the layout algorithms. */
        public unlockNode(
            /** Node ID or object */
            id: (string|Configuration.ItemsChartNode)): void;
        /** Updates the chart settings. Only the settings that have to be changed should be passed. Note that some arrays
        and dictionaries (such as `data`) are merged by the ID values - if instead they should be replaced, use
        [`replaceSettings()`](#doc_replaceSettings) method. */
        public updateSettings(changes: Configuration.NetChartSettings): NetChart;
        /** Updates (recalculates) the style for the whole chart or specific objects matching the given IDs. */
        public updateStyle(
            /** A list of IDs for the nodes and links which need their style recalculated */
            objects?: Array<string>): void;
        public zoom(zoomValue: number): number;
        public zoomIn(objects: Array<string>, animate?: boolean): void;
    }
    export class PieChart {
        public constructor(settings: Configuration.PieChartSettings);
        /** Adds the given data to whatever data the chart has currently loaded. The chart will automatically be updated
        to display this new data if it falls within the currently visible bounds. */
        public addData(data: Configuration.PieChartDataObjectRoot, sourceId?: string): void;
        public expandSlice(slice: Configuration.PieChartSlice): boolean;
        public getActivePie(): Configuration.PieChartPie;
        public getActivePies(): Array<Configuration.PieChartPie>;
        public getActiveSlices(): Array<Configuration.PieChartSlice>;
        public getPie(): Array<string>;
        public getPieOffset(): number;
        public getSliceDimensions(sliceId: Array<string>, showPartial?: boolean): {
                centerX: number;
                centerY: number;
                r0: number;
                r1: number;
                a0: number;
                a1: number;
            };
        /** Adds event listener. */
        public on(
            /** The type of the event for which the listener will be added. See method overloads for valid values. */
            name: string, 
            /** The callback function. It receives two arguments - the mouse event data and a separate object containing chart specific information. */
            listener: (event: Configuration.BaseMouseEvent, args: Configuration.BaseChartEventArguments) => void): void;
        public on(name: "chartUpdate", listener: (event: Configuration.BaseMouseEvent, args: Configuration.PieChartChartEventArguments) => void): void;
        public on(name: "click", listener: (event: Configuration.BaseMouseEvent, args: Configuration.PieChartChartClickEventArguments) => void): void;
        public on(name: "doubleClick", listener: (event: Configuration.BaseMouseEvent, args: Configuration.PieChartChartClickEventArguments) => void): void;
        public on(name: "error", listener: (
                /** An empty mouse event. */
                event: Configuration.BaseMouseEvent, args: Configuration.BaseChartErrorEventArguments) => void): void;
        public on(name: "hoverChange", listener: (event: Configuration.BaseMouseEvent, args: Configuration.PieChartChartEventArguments) => void): void;
        public on(name: "positionChange", listener: (event: Configuration.BaseMouseEvent, args: Configuration.PieChartChartEventArguments) => void): void;
        public on(name: "rightClick", listener: (event: Configuration.BaseMouseEvent, args: Configuration.PieChartChartClickEventArguments) => void): void;
        public on(name: "selectionChange", listener: (event: Configuration.BaseMouseEvent, args: Configuration.PieChartChartEventArguments) => void): void;
        public on(name: "settingsChange", listener: (event: Configuration.BaseMouseEvent, args: Configuration.BaseChartSettingsChangeEventArguments) => void): void;
        public on(name: "tripleClick", listener: (event: Configuration.BaseMouseEvent, args: Configuration.PieChartChartClickEventArguments) => void): void;
        public removeData(data: Configuration.PieChartDataObjectRoot, sourceId?: string): void;
        public replaceData(data: Configuration.PieChartDataObjectRoot, sourceId?: string): void;
        /** Updates the chart settings but instead of merging some settings that are arrays or dictionaries (such as `data`)
        these collections are replaced completely. For example, this allows removal of series or value axis within TimeChart. */
        public replaceSettings(changes: Configuration.PieChartSettings): PieChart;
        /** Retrieves or updates the selected slices on the chart. */
        public selection(
            /** The slices or their IDs that should be selected. Any currently selected slices that are not present will be unselected. */
            selected?: Array<(string|Configuration.PieChartSlice)>): Array<Configuration.PieChartSlice>;
        public setPie(pieId: Array<string>, offset?: number): PieChart;
        public setPieOffset(offset: number): PieChart;
        /** Lists the predefined themes for the chart. These can be used within the settings objects or via the `customize()` method:
        
        ```javascript 
        var chart = new ZoomCharts.$this({ theme: ZoomCharts.$this.dark });
        chart.updateSettings({ theme: ZoomChart.$this.dark });
        chart.customize("dark");
        ``` */
        public static themes: {
            bevel?: Configuration.PieChartSettings;
            dark?: Configuration.PieChartSettings;
            flat?: Configuration.PieChartSettings;
            gradient?: Configuration.PieChartSettings;
            raised?: Configuration.PieChartSettings;
        };
        public updateFilter(): PieChart;
        /** Updates the chart settings. Only the settings that have to be changed should be passed. Note that some arrays
        and dictionaries (such as `data`) are merged by the ID values - if instead they should be replaced, use
        [`replaceSettings()`](#doc_replaceSettings) method. */
        public updateSettings(changes: Configuration.PieChartSettings): PieChart;
    }
    export class TimeChart {
        public constructor(settings: Configuration.TimeChartSettings);
        /** Adds the given data to whatever data the chart has currently loaded. The chart will automatically be updated
        to display this new data if it falls within the currently visible bounds. */
        public addData(data: Configuration.TimeChartDataObject, sourceId?: string): void;
        public displayUnit(unit: string, animate?: boolean, rescale?: boolean, rescaleCenter?: string): string;
        public exportVisibleData(): Array<Array<(string|number)>>;
        /** Adds event listener. */
        public on(
            /** The type of the event for which the listener will be added. See method overloads for valid values. */
            name: string, 
            /** The callback function. It receives two arguments - the mouse event data and a separate object containing chart specific information. */
            listener: (event: Configuration.BaseMouseEvent, args: Configuration.BaseChartEventArguments) => void): void;
        /** Adds an event listener for when the scroll animation completes. */
        public on(name: "animationDone", listener: (event: Configuration.BaseMouseEvent, args: Configuration.TimeChartChartEventArguments) => void): void;
        public on(name: "chartUpdate", listener: (event: Configuration.BaseMouseEvent, args: Configuration.TimeChartChartEventArguments) => void): void;
        public on(name: "click", listener: (event: Configuration.BaseMouseEvent, args: Configuration.TimeChartChartEventArguments) => void): void;
        public on(name: "doubleClick", listener: (event: Configuration.BaseMouseEvent, args: Configuration.TimeChartChartEventArguments) => void): void;
        public on(name: "error", listener: (
                /** An empty mouse event. */
                event: Configuration.BaseMouseEvent, args: Configuration.BaseChartErrorEventArguments) => void): void;
        public on(name: "hoverChange", listener: (event: Configuration.BaseMouseEvent, args: Configuration.TimeChartChartEventArguments) => void): void;
        public on(name: "positionChange", listener: (event: Configuration.BaseMouseEvent, args: Configuration.TimeChartChartEventArguments) => void): void;
        public on(name: "rightClick", listener: (event: Configuration.BaseMouseEvent, args: Configuration.TimeChartChartEventArguments) => void): void;
        public on(name: "selectionChange", listener: (event: Configuration.BaseMouseEvent, args: Configuration.TimeChartChartEventArguments) => void): void;
        public on(name: "settingsChange", listener: (event: Configuration.BaseMouseEvent, args: Configuration.BaseChartSettingsChangeEventArguments) => void): void;
        public on(name: "tripleClick", listener: (event: Configuration.BaseMouseEvent, args: Configuration.TimeChartChartEventArguments) => void): void;
        public removeData(data: Configuration.TimeChartDataObject, sourceId?: string): void;
        public replaceData(data: Configuration.TimeChartDataObject, sourceId?: string): void;
        /** Replaces the existing series configuration with new one instead of merging it like `updateSettings`.
        @deprecated Use `replaceSettings({series: seriesData})` instead. */
        public replaceSeries(series: Array<Configuration.LinearChartSettingsSeries>): TimeChart;
        /** Updates the chart settings but instead of merging some settings that are arrays or dictionaries (such as `data`)
        these collections are replaced completely. For example, this allows removal of series or value axis within TimeChart. */
        public replaceSettings(changes: Configuration.TimeChartSettings): TimeChart;
        public scroll(amount: string, animate?: boolean): void;
        public selection(from: number, to: number): Array<number>;
        /** Set displayed time range using period and anchor.
        See settings for possible period / anchor values. */
        public setDisplayPeriod(period: string, anchor: string, animate: boolean): void;
        /** Lists the predefined themes for the chart. These can be used within the settings objects or via the `customize()` method:
        
        ```javascript 
        var chart = new ZoomCharts.$this({ theme: ZoomCharts.$this.dark });
        chart.updateSettings({ theme: ZoomChart.$this.dark });
        chart.customize("dark");
        ``` */
        public static themes: {
            dark?: Configuration.TimeChartSettings;
            flat?: Configuration.TimeChartSettings;
            gradient?: Configuration.TimeChartSettings;
            round?: Configuration.TimeChartSettings;
            static?: Configuration.TimeChartSettings;
        };
        /** Gets or sets the  displayed time range in JavaScript timestamp (milliseconds). */
        public time(from?: number, to?: number, animate?: boolean): [number, number];
        /** Updates the chart settings. Only the settings that have to be changed should be passed. Note that some arrays
        and dictionaries (such as `data`) are merged by the ID values - if instead they should be replaced, use
        [`replaceSettings()`](#doc_replaceSettings) method. */
        public updateSettings(changes: Configuration.TimeChartSettings): TimeChart;
        public zoomIn(unit: string, center?: number, animate?: boolean): void;
        public zoomOut(unit: string, animate?: boolean): void;
    }
    /* tslint:enable */
}

declare class FacetChart extends ZoomCharts.FacetChart { }
declare class GeoChart extends ZoomCharts.GeoChart { }
declare class NetChart extends ZoomCharts.NetChart { }
declare class PieChart extends ZoomCharts.PieChart { }
declare class TimeChart extends ZoomCharts.TimeChart { }

declare module ZoomCharts {
    export interface Dictionary<TValue> {
        [key: string]: TValue;
    }

    export interface NumberDictionary<TValue> {
        [key: number]: TValue;
    }

    export interface GradientDefinition extends Array<[number, string]> {
    }

    export interface IRectangle {
        x0: number;
        y0: number;
        x1: number;
        y1: number;
    }

    export interface IGeoRectangle {
        east: number;
        west: number;
        north: number;
        south: number;
    }

    export interface IColor {
        R: number;
        G: number;
        B: number;
        A: number;

        /** The cached result of this color converted to LAB */
        _lab?: { L: number; A: number; B: number; };
    }
}

interface Window {
    /** The name of the license to be used by all charts on the page. This value is usually in form `ZCX-foobar: production license for *.example.org` */
    ZoomChartsLicense: string;
    /** The license key to match the license name. This is a 512 character hexadecimal string. */
    ZoomChartsLicenseKey: string;
}
