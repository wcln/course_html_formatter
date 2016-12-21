require 'clipboard' # https://github.com/janlelis/clipboard
require 'uri'
require 'time'
require 'open-uri'
require 'launchy'

###### SETTINGS #######
CURRENT_COURSE = ''
CURRENT_SECTION = ''
GENERATE_LOG = true
#######################

CONTENT_SERVER_DOMAIN_NAME = ''

contents = Clipboard.paste.encode('UTF-8') # fetch clipboard contents

target = open('temp.txt', 'w') # clear temp file
target.close

URI.extract(contents).each do |link| # find resources in html
  if link =~ /LOR|pluginfile.php|draftfile.php/
    puts "FOUND: #{link}"
    unless link.include?(CURRENT_COURSE) && link.include?("#{CURRENT_SECTION}/") # only update if necessary
      filename = File.basename(link)
      src = "/html#{link.split(CONTENT_SERVER_DOMAIN_NAME.downcase)[-1]}"


      if link =~ /pluginfile.php|draftfile.php/
        Launchy.open(link) # open browser and download image (browser set to download to correct folder)
		filename.gsub! '%20', ' '
      else # file is in LOR, simply download
		filename.gsub! '%20', '_'
        open(link) {|f|
          File.open(filename, 'wb') do |file|
            file.puts f.read
          end
        }
      end

      # write FTP src and dest to file for JAVA
      open('temp.txt', 'a') { |f|

        if link =~ /pluginfile.php|draftfile.php/
          f.puts filename
        else
          f.puts filename
        end
        f.puts "/html/LOR/media/#{CURRENT_COURSE}/#{CURRENT_SECTION}/"
      }
	
	  dest = "/html/LOR/media/#{CURRENT_COURSE}/#{CURRENT_SECTION}/#{filename}"
      new_link =  dest.sub! '/html', 'http://www.BCLearningNetwork.com' # generate the new file link
      $new_contents = contents.gsub(link, new_link) # replace old link with new link
      contents = $new_contents # update file contents for next loop

      puts "UPDATED: #{filename}"

      ######## GENERATE LOG FILE CODE
      if GENERATE_LOG
        open('log.txt', 'a') { |f| # append to end of log file
          f.puts "[#{Time.now.utc.iso8601}] MOVED \"#{filename}\" FROM #{link} TO #{new_link}\n\n"
        }
      end
    end
  end
end
begin
  Clipboard.copy $new_contents # send new html to clipboard
rescue
  puts 'NO CHANGES NEEDED!'
end
