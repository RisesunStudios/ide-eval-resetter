#!/bin/sh
# reset jetbrains ide evals for now

OS_NAME=$(uname -s)
JB_PRODUCTS="IntelliJIdea CLion PhpStorm GoLand PyCharm WebStorm Rider DataGrip RubyMine AppCode"

if [ "$OS_NAME" = "Darwin" ]; then
	echo 'macOS:'

	for PRD in $JB_PRODUCTS; do
    rm -rf ~/Library/Preferences/${PRD}*/eval
    rm -rf ~/Library/Application\ Support/${PRD}*/eval
    rm -rf ~/Library/Application\ Support/JetBrains/${PRD}*/eval
	done
elif [ "$OS_NAME" = "Linux" ]; then
	echo 'Linux:'

	for PRD in $JB_PRODUCTS; do
	  rm -rf ~/.${PRD}*/config/eval
    rm -rf ~/.config/${PRD}*/eval
    rm -rf ~/.config/JetBrains/${PRD}*/eval
	done
else
	echo 'unsupported'
	exit
fi

echo 'done.'
