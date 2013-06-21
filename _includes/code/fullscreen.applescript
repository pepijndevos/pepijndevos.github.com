tell application "System Events"
	set frontmostProcess to name of first process where it is frontmost
end tell

tell application "Finder"
	set screenSize to bounds of window of desktop
end tell

tell application frontmostProcess
	set bounds of the first window to screenSize
end tell