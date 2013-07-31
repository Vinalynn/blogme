<@compress single_line=true>
<style>
    #blog_header{
        width: 100%;
        /*font-size: 11pt;*/
        /*padding-top: 15px;*/
    }
    #blog_header #sitename-td{
        padding:4px 0 4px 0;
        line-height: 1em;
        /*font-size: 11pt;*/
    }
    #blogname{
        font-family: 'Source Code Pro', sans-serif;
        text-decoration: none;
        font-size: 30pt;
        font-style: italic;
        color:#000088;
    }
    #blogname h1{
        /*padding-bottom: -10px;*/
        display: block;
        font-size: 2em;
        -webkit-margin-before: 0.67em;
        -webkit-margin-after: 0.67em;
        -webkit-margin-start: 0px;
        -webkit-margin-end: 0px;
        font-weight: normal;
        /*line-height: 50px;*/
    }

    #nav-bar{
        width: 100%;
        /*height: auto;*/
        display: inline-block;
        background-color: #198601;
        border-radius: 8px 0;
    }
    #blog-bar{
        margin-bottom: 16px;
    }

    #blog_header #nav-bar li{
        list-style-type: none;
        list-style-position: outside;
        list-style-image: none;
        cursor: pointer;
        display: block;
        float: left;
        margin: 0;
        padding: 1px 5px 1px 5px;
        z-index: 999;
        text-align: -webkit-match-parent;
    }
    #blog_header #nav-bar li:hover{
        display:block;background:dimgray;
        /*-moz-opacity:.90;*/
        /*opacity:0.9*/
    }

    #blog_header #nav-bar li a{
        text-decoration: none;
        color: #EEE;
        padding: 4px 6px;
        margin: 0;
        font-size: 13px;
        /*font-weight: bold;*/
        display: block;
        text-shadow: 1px 1px 2px #333;
        z-index: 999;

    }
</style>
<div id="blog_header">
    <div id="blog-bar" class="clearfloat">
    <table width="100%">
        <tr>
            <td colspan="2" id="sitename-td"></td>
        </tr>
        <tr>
            <td id="blogname-td" style="width: 60%">
                <h1>
                <a id="blogname" href="#" title="Human. Question. Live">Human.Question.Live</a>
                </h1>
            </td>
            <td>

            </td>
        </tr>
        <tr>
             <td>
                 <a style="font-size: 14px;padding-left: 10px;">
                     Articles, tutorials and news on technologies for Highly Scalable Systems.</a>
             </td>
        </tr>
    </table>
    </div>
    <div id="nav-bar" class="clearfloat">
        <ul style="margin-left: 8px">
            <li><a href="#" >主页</a></li>
            <li><a href="#">新闻</a></li>
            <li><a href="#">关于</a></li>
            <li><a href="#">订阅</a></li>
        </ul>
    </div>
</div>
</@compress>