PR['registerLangHandler'](
    PR['createSimpleLexer'](
        // Shortcut patterns.
        [
         // The space production <s>
         [PR['PR_PLAIN'],       /^[ \t\r\n\f]+/, null, ' \t\r\n\f']
        ],
        // Fall-through patterns.
        [
         ['typ', /^(http|https|mailto|ftp):/i],
         ['kwd', /^(\?|&)[^=]+=?/i],
         ['lit', /^[^=&]+/i]
        ]),
    ['url', 'uri']);