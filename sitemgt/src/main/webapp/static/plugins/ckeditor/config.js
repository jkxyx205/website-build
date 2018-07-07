CKEDITOR.editorConfig = function( config ) {
    config.language = 'zh-cn';
    //remove button
    config.removePlugins = 'elementspath';
    config.resize_enabled = false;

    config.toolbarGroups = [
        { name: 'document', groups: [ 'mode', 'document', 'doctools' ] },
        { name: 'clipboard', groups: [ 'clipboard', 'undo' ] },
        { name: 'editing', groups: [ 'find', 'selection', 'spellchecker', 'editing' ] },
        { name: 'forms', groups: [ 'forms' ] },
        { name: 'styles', groups: [ 'styles' ] },
        '/',
        { name: 'basicstyles', groups: [ 'basicstyles', 'cleanup' ] },
        { name: 'colors', groups: [ 'colors' ] },
        { name: 'paragraph', groups: [ 'list', 'indent', 'blocks', 'align', 'bidi', 'paragraph' ] },
        { name: 'links', groups: [ 'links' ] },
        { name: 'insert', groups: [ 'insert' ] },
        { name: 'tools', groups: [ 'tools' ] },
        '/',
        { name: 'others', groups: [ 'others' ] },
        // { name: 'about', groups: [ 'about' ] }
    ];

    //config.removeButtons = 'Source,Save,NewPage,Preview,Print,Templates,Cut,Copy,Paste,PasteText,PasteFromWord,Find,Replace,SelectAll,Scayt,Form,Checkbox,Radio,TextField,Textarea,Select,Button,ImageButton,HiddenField,Subscript,Superscript,CreateDiv,BidiLtr,BidiRtl,Language,Anchor,Flash,Smiley,SpecialChar,PageBreak,Iframe,ShowBlocks,About';

    // config.filebrowserImageUploadUrl= ctx +"/ckeditor/uploadImg?u_folder=site/"+parent.webId+"&u_categoryId=1&webId=" + parent.webId; //待会要上传的action或servlet

    // config.extraPlugins = 'file';
};


