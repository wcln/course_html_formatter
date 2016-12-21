require 'clipboard'
Clipboard.copy(Clipboard.paste.encode('UTF-8').gsub('<p><img','<p align="center"><img'))