<!DOCTYPE html>

<html>
<head>
    <title></title>
    <#include "common/env.ftl">
    <style>
        .td_width_30{
            width: 30%;
        }
        .td_width_50{

            width: 50%;
        }

        .td_width_40{

            width: 40%;
        }

        .td_align_right{
            text-align: right;
        }

        .td_multi_height_50{
            height: 50px;
        }


    </style>
</head>
</html>
<body>
<div class="container">
<#include "common/header.ftl">

    <div class="main-wrapper">
        <div class="post_area" style="margin: 20px auto">
            <table border="0">
                <tr>
                    <td class="td_multi_height_50 td_width_40 td_align_right td_font_size_default">
                        <label>英文路径：</label></td>
                    <td>
                        <div class="d_input_400px">
                            <label><input name="en_url" type="text"
                                          id="en_url" placeholder="输入文章标题英文路径"/>
                            </label>
                        </div>
                    </td>
                </tr>
                <tr>
                    <td class="td_multi_height_50 td_width_40 td_align_right td_font_size_default">
                        <label>文章标题：</label>
                    </td>
                    <td>
                        <div class="d_input_400px">
                            <label><input name="article_title" type="text"
                                          id="article_title" placeholder="输入文章标题"/>
                            </label>
                        </div>
                    </td>
                </tr>

                <tr>
                    <td class="td_multi_height_50 td_width_40 td_align_right td_font_size_default">
                        <label>标签：</label></td>
                    <td>
                        <div class="d_input_400px">
                            <label><input name="article_tags" type="text"
                                          id="article_tags" placeholder="输入标签，用逗号分隔"/>
                            </label>
                        </div>
                    </td>
                </tr>
            </table>

            <table style="width: 100%;">
                <tr>
                    <td style="width: 13%;"></td>
                    <td>    <label>
                        <textarea name="article_content_area" id="article_content_area"></textarea>
                    </label>
                    </td>
                </tr>
            </table>
            <div style="text-align: center; margin-top: 20px">
                <a href="javascript:submit()">Submit</a>
            </div>
        </div>
    </div>
</div>

<script type="text/javascript">
    var editor;
    KindEditor.ready(function (K) {
        var options = {

        };
        editor = K.create('textarea[name="article_content_area"]', options);
    })

    function submit() {
        var param = {
            "enUrl": $("#en_url").val(),
            "aTitle": $("#article_title").val(),
            "aTags": $("#article_tags").val(),
            "aHtmlValue": editor.html(),
            "aTextValue": editor.text()
        };
        postToServer(param, "POST", false, "/save-article.cgi", {
            "success": function (data) {
                alert(data);
            }
        });
    }
</script>
</body>