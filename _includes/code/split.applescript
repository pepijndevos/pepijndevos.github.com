tell application "System Events"
	set frontmostProcess to first process where it is frontmost
	set visible of frontmostProcess to false
	repeat while (frontmostProcess is frontmost)
		delay 0.1
	end repeat
	set secondFrontmost to name of first process where it is frontmost
	set frontmost of frontmostProcess to true
	set frontmostApplication to name of frontmostProcess
end tell

tell application "Finder"
	set screenSize to bounds of window of desktop
	set screenWidth to item 3 of screenSize
	set screenHeight to item 4 of screenSize
end tell

tell application frontmostApplication
	set bounds of the first window to {0, 0, screenWidth / 2, screenHeight}
end tell

tell application secondFrontmost
	set bounds of the first window to {screenWidth / 2, 0, screenWidth, screenHeight}
end tell