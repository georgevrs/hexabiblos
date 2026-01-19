# Hexabiblos Enterprise Chatbot UI - Stage 2

A polished, professional chatbot web application built with Jakarta EE (JSF + PrimeFaces) for enterprise environments. 

**Stage 1 (COMPLETE)**: Fully functional UI with client-side simulated chat logic.

**Stage 2 (CURRENT)**: Server-side chat state management with Google Gemini AI integration using the official Java SDK.

## Project Overview

**Technology Stack:**
- Jakarta Faces (JSF) 4.0.1
- PrimeFaces 13.0.0 (Jakarta variant)
- Google Gemini AI SDK 1.0.0
- Maven 3.x
- Java 17
- WildFly 27.x (preferred) or Tomcat 10.x

**Stage 1 Features (COMPLETE):**
- ✅ Enterprise-grade responsive UI
- ✅ Real-time message display with user/bot styling
- ✅ Typing indicator simulation
- ✅ Quick reply chips
- ✅ Enter key to send (Shift+Enter for newline)
- ✅ Auto-scroll to latest message
- ✅ Clear chat functionality
- ✅ Client-side simulated responses

**Stage 2 Features (CURRENT):**
- ✅ Server-side chat state management (JSF session-scoped bean)
- ✅ Google Gemini AI integration via official Java SDK
- ✅ PrimeFaces AJAX for seamless UI updates
- ✅ Conversation history management
- ✅ Demo mode fallback (when API key not configured)
- ✅ Input validation (max 2000 characters)
- ✅ Error handling and timeout protection

## Prerequisites for Windows (Fresh Installation)

Since this is a Windows machine new to Java stacks, follow these steps:

### 1. Install Java JDK 17

1. Download **Oracle JDK 17** or **OpenJDK 17**:
   - **Option A (Oracle)**: https://www.oracle.com/java/technologies/javase/jdk17-archive-downloads.html
   - **Option B (OpenJDK - Recommended)**: https://adoptium.net/temurin/releases/?version=17
   
2. Run the installer and follow the wizard
3. **Set JAVA_HOME environment variable:**
   - Open **System Properties** → **Advanced** → **Environment Variables**
   - Under **System Variables**, click **New**
   - Variable name: `JAVA_HOME`
   - Variable value: `C:\Program Files\Java\jdk-17` (or your JDK installation path)
   - Click **OK**
4. **Add Java to PATH:**
   - Edit the **Path** variable in System Variables
   - Add: `%JAVA_HOME%\bin`
   - Click **OK** on all dialogs
5. **Verify installation:**
   ```powershell
   java -version
   javac -version
   ```
   Should show version 17.x.x

### 2. Install Apache Maven

1. Download Maven from: https://maven.apache.org/download.cgi
   - Download the **Binary zip archive** (e.g., `apache-maven-3.9.5-bin.zip`)
2. Extract to `C:\Program Files\Apache\maven` (or your preferred location)
3. **Set M2_HOME environment variable:**
   - System Variables → **New**
   - Variable name: `M2_HOME`
   - Variable value: `C:\Program Files\Apache\maven` (your Maven path)
4. **Add Maven to PATH:**
   - Edit **Path** variable
   - Add: `%M2_HOME%\bin`
5. **Verify installation:**
   ```powershell
   mvn -version
   ```
   Should show Maven version and Java 17

### 3. Install WildFly Application Server

**Option A: Manual Installation (Recommended for Production-like Setup)**

1. Download WildFly 27.x from: https://www.wildfly.org/downloads/
   - Choose **Full Distribution** (ZIP file)
2. Extract to `C:\wildfly` (or your preferred location)
3. **Set JBOSS_HOME environment variable (optional but recommended):**
   - System Variables → **New**
   - Variable name: `JBOSS_HOME`
   - Variable value: `C:\wildfly`
4. **Start WildFly:**
   ```powershell
   cd C:\wildfly\bin
   .\standalone.bat
   ```
   Wait for message: `WFLYSRV0025: WildFly Full 27.x.x started`

**Option B: Use Embedded WildFly via Maven (Easier for Development)**

No separate installation needed! The project includes `cargo-maven-plugin` for embedded deployment.

## Building the Project

### Quick Start (Recommended)

Use the provided scripts to automatically set up Java environment and run the application:

**PowerShell:**
```powershell
.\run.ps1
```

**Command Prompt:**
```cmd
run.bat
```

These scripts will:
- Automatically set JAVA_HOME to the correct JDK
- Verify Java and Maven are accessible
- Check for .env file
- Build and deploy the application

### Manual Build

1. **Open PowerShell or Command Prompt**
2. **Set Java environment** (if not already set):
   ```powershell
   $env:JAVA_HOME="C:\Program Files\Eclipse Adoptium\jdk-17.0.17.10-hotspot"
   $env:PATH="$env:JAVA_HOME\bin;$env:PATH"
   ```
3. **Navigate to project directory:**
   ```powershell
   cd C:\Users\georg\Desktop\Interviews\Hexabiblos
   ```
4. **Build the WAR file:**
   ```powershell
   mvn clean package
   ```
   This will:
   - Download all dependencies (first time may take a few minutes)
   - Compile Java code (if any)
   - Package the WAR file to `target/chatbot-ui.war`

## Prerequisites Check

Before building, ensure:
1. **Java 17 JDK** is installed (not JRE)
2. **Maven** is installed and in PATH
3. **WildFly 27** is installed at `C:\wildfly` (or update path in `pom.xml`)

**Quick verification:**
```powershell
java -version        # Should show Java 17
mvn -version         # Should show Maven version AND Java 17
```

**Important**: If `mvn -version` shows Java 8, set JAVA_HOME:
```powershell
$env:JAVA_HOME="C:\Program Files\Eclipse Adoptium\jdk-17.0.17.10-hotspot"
$env:PATH="$env:JAVA_HOME\bin;$env:PATH"
mvn -version  # Verify it now shows Java 17
```

## Stage 2: Gemini API Configuration

### Setting the Gemini API Key

The application requires a Google Gemini API key to generate AI responses. You can get one from: https://aistudio.google.com/apikey

**Option 1: .env File (Easiest for Development - Recommended)**

1. Copy the example file:
   ```powershell
   Copy-Item .env.example .env
   ```

2. Edit `.env` and add your API key (make sure there are NO spaces around the `=`):
   ```
   GEMINI_API_KEY=your-actual-api-key-here
   ```
   **Important**: No quotes, no spaces. Just: `GEMINI_API_KEY=your-key-here`

3. The application will automatically read from `.env` file when it starts.

**Note**: When running in WildFly, the `.env` file must be in the project root directory (where `pom.xml` is). The application searches multiple locations, but the project root is checked first.

**If .env file doesn't work in WildFly** (most reliable):
1. Copy `.env` to WildFly directory:
   ```powershell
   Copy-Item .env C:\wildfly\.env
   ```
2. Or use environment variables (see Option 3 below) - this is more reliable for WildFly

**Troubleshooting .env file**:
- Check WildFly console logs for "✓ Loaded .env file from: ..." or "✗ No API key found" messages
- If you see "No .env file found", try copying it to `C:\wildfly\.env`
- Verify the file format: `GEMINI_API_KEY=your-key-here` (no quotes, no spaces around `=`)
- Verify the file has no BOM (Byte Order Mark) - save as UTF-8 without BOM
- Make sure there are no trailing spaces in the API key value
- Check WildFly console for debug messages showing API key length

**Note**: The `.env` file is gitignored for security. Never commit your actual API key!

**Option 3: Environment Variable**

Set the `GEMINI_API_KEY` environment variable before starting WildFly:

**Windows PowerShell:**
```powershell
$env:GEMINI_API_KEY="your-api-key-here"
mvn clean package cargo:run
```

**Windows Command Prompt:**
```cmd
set GEMINI_API_KEY=your-api-key-here
mvn clean package cargo:run
```

**Permanent (System-wide):**
1. Open **System Properties** → **Advanced** → **Environment Variables**
2. Under **System Variables**, click **New**
3. Variable name: `GEMINI_API_KEY`
4. Variable value: `your-api-key-here`
5. Click **OK** on all dialogs
6. Restart WildFly/Maven

**Option 4: WildFly Configuration**

You can also set environment variables in WildFly's `standalone.conf.bat`:
```batch
set "GEMINI_API_KEY=your-api-key-here"
```

**Important**: When editing `standalone.conf.bat`, also ensure it uses Java 17:
```batch
set "JAVA_HOME=C:\Program Files\Eclipse Adoptium\jdk-17.0.17.10-hotspot"
set "JAVA=%JAVA_HOME%\bin\java"
```
This ensures WildFly (and Cargo) use Java 17 instead of any hardcoded Java 8 path.

### Optional Configuration

- **GEMINI_MODEL**: Model to use (default: `gemini-3-flash-preview`)
  ```powershell
  $env:GEMINI_MODEL="gemini-2.5-flash"
  ```

- **GEMINI_MAX_TURNS**: Maximum conversation turns to include in context (default: `20`)
  ```powershell
  $env:GEMINI_MAX_TURNS="15"
  ```

### Configuration Priority

The application checks for API key in this order:
1. `.env` file (in project root)
2. `GEMINI_API_KEY` environment variable
3. `GOOGLE_API_KEY` environment variable (SDK default)

### Demo Mode

If `GEMINI_API_KEY` is not set in any of the above locations, the application runs in **Demo Mode**:
- Shows a demo mode indicator in the UI
- Returns a deterministic message: "Gemini API key not configured. Running in demo mode."
- All other features work normally (UI, chat state, etc.)

## Running the Application

### Method 1: Using the Run Script (Easiest - Recommended)

**PowerShell:**
```powershell
.\run.ps1
```

**Command Prompt:**
```cmd
run.bat
```

The script automatically handles Java environment setup and runs the build + deployment.

### Method 2: Embedded WildFly (Manual)

```powershell
mvn clean package cargo:run
```

This will:
- Build the WAR
- Start an embedded WildFly server
- Deploy the application
- Open browser automatically (or navigate to http://localhost:8080/chatbot-ui/chat.xhtml)

**To stop:** Press `Ctrl+C` in the terminal

### Method 3: Manual WildFly Deployment

1. **Start WildFly** (if not already running):
   ```powershell
   cd C:\wildfly\bin
   .\standalone.bat
   ```

2. **Deploy the WAR:**
   - Copy `target/chatbot-ui.war` to `C:\wildfly\standalone\deployments\`
   - WildFly will automatically detect and deploy it
   - Watch the console for deployment success message

3. **Access the application:**
   - Open browser: http://localhost:8080/chatbot-ui/chat.xhtml

4. **Undeploy:**
   - Delete `chatbot-ui.war` from the deployments folder, or
   - Create `chatbot-ui.war.dodeploy` file (empty) to trigger undeployment

### Method 4: Using Tomcat 10.x (Alternative)

If you prefer Tomcat:

1. Download Tomcat 10.1.x from: https://tomcat.apache.org/download-10.cgi
2. Extract to `C:\tomcat`
3. Copy `target/chatbot-ui.war` to `C:\tomcat\webapps\`
4. Start Tomcat: `C:\tomcat\bin\startup.bat`
5. Access: http://localhost:8080/chatbot-ui/chat.xhtml

## Project Structure

```
Hexabiblos/
├── pom.xml                          # Maven configuration
├── README.md                        # This file
└── src/
    └── main/
        └── webapp/
            ├── WEB-INF/
            │   ├── web.xml          # Web application configuration
            │   └── faces-config.xml # JSF configuration
            ├── index.xhtml          # Redirect to chat page
            ├── chat.xhtml           # Main chatbot UI page
            └── resources/
                ├── css/
                │   └── chat.css     # Enterprise styling
                └── js/
                    └── chat.js      # Client-side chat logic
```

## Testing Checklist

Use this checklist to manually verify all UI features:

### ✅ Basic Functionality

- [ ] **Page Loads**: Navigate to `/chatbot-ui/chat.xhtml` - page displays without errors
- [ ] **Welcome Message**: Initial bot welcome message is visible
- [ ] **Layout**: Chat interface displays with header, message area, and input section
- [ ] **Responsive**: Resize browser window - layout adapts (test on mobile viewport)

### ✅ Message Sending

- [ ] **Send Button**: Type a message, click "Send" button - message appears as user bubble
- [ ] **Enter Key**: Type a message, press Enter - message sends
- [ ] **Shift+Enter**: Type message, press Shift+Enter - adds newline instead of sending
- [ ] **Empty Message**: Try sending empty message - should be ignored (no message added)
- [ ] **Whitespace**: Type only spaces, send - should be ignored

### ✅ Bot Responses

- [ ] **Typing Indicator**: After sending message, "typing dots" appear
- [ ] **Response Delay**: Bot response appears after 600-1200ms delay
- [ ] **Message Styling**: Bot messages appear in gray bubble (left-aligned), user messages in blue (right-aligned)
- [ ] **Timestamps**: Each message shows timestamp below it

### ✅ Keyword Responses

- [ ] **Help Keyword**: Send message containing "help" - bot responds with help information
- [ ] **Features Keyword**: Send message containing "features" - bot lists available features
- [ ] **Reset Keyword**: Send message containing "reset" - bot acknowledges reset
- [ ] **Default Response**: Send any other message - bot gives generic friendly response

### ✅ Quick Reply Chips

- [ ] **Help Chip**: Click "Help" chip - sends "Help" message and triggers help response
- [ ] **Show Features Chip**: Click "Show Features" - sends message and shows features
- [ ] **Reset Chip**: Click "Reset" - clears chat and shows reset message

### ✅ Clear Chat

- [ ] **Clear Functionality**: Click "Reset" chip or send "reset" - chat clears (keeps welcome message)
- [ ] **State Reset**: After clear, new messages work normally

### ✅ Auto-Scroll

- [ ] **Scroll Behavior**: When new message is added, chat automatically scrolls to bottom
- [ ] **Manual Scroll**: Scroll up, send new message - auto-scrolls to bottom again

### ✅ UI Polish

- [ ] **Styling**: Colors are professional (blue/gray theme, no random colors)
- [ ] **Animations**: Messages fade in smoothly
- [ ] **Hover Effects**: Buttons show hover states
- [ ] **Focus States**: Textarea shows focus border
- [ ] **Scrollbar**: Message area has styled scrollbar

## Stage 2: Manual Testing Checklist

Use this checklist to verify Stage 2 functionality:

### ✅ Basic Functionality

- [ ] **Demo Mode**: Run without `GEMINI_API_KEY` - should show demo mode indicator and demo message
- [ ] **API Mode**: Run with `GEMINI_API_KEY` - should receive real Gemini responses
- [ ] **Welcome Message**: Initial assistant welcome message appears on page load
- [ ] **Session Persistence**: Refresh page - conversation history should persist (session-scoped)

### ✅ Message Sending

- [ ] **Send Button**: Type message, click "Send" - message appears, AI response follows
- [ ] **Enter Key**: Type message, press Enter - message sends (Shift+Enter adds newline)
- [ ] **Empty Message**: Try sending empty message - should be ignored
- [ ] **Long Message**: Type message > 2000 chars - should show error message
- [ ] **Loading State**: Input field and send button disabled while loading

### ✅ AI Integration

- [ ] **Real Response**: With API key, receive actual Gemini-generated response
- [ ] **Conversation Context**: Send follow-up message - AI should remember previous context
- [ ] **Error Handling**: Temporarily disconnect network - should show error message, not crash
- [ ] **Timeout Protection**: Long-running requests should timeout gracefully

### ✅ Quick Replies

- [ ] **Help**: Click "Help" - sends "Help" message and gets AI response
- [ ] **Show Features**: Click "Show Features" - sends message and gets response
- [ ] **Reset**: Click "Reset" - clears conversation, shows new welcome message

### ✅ UI Behavior

- [ ] **Typing Indicator**: Shows while waiting for AI response
- [ ] **Auto-scroll**: Automatically scrolls to latest message after response
- [ ] **Message Styling**: User messages (right/blue), Assistant messages (left/gray)
- [ ] **Timestamps**: Each message shows timestamp
- [ ] **HTML Escaping**: Special characters in messages are properly escaped

### ✅ Error Scenarios

- [ ] **Missing API Key**: App runs in demo mode without errors
- [ ] **Invalid API Key**: Should handle gracefully (may show error or fallback)
- [ ] **Network Error**: Should show user-friendly error message
- [ ] **UI Doesn't Freeze**: Errors don't prevent further interactions

## Optional: Automated UI Testing

For Stage 1, manual testing is sufficient. However, if you want to add automated tests later, consider:

### Playwright (Recommended)

```powershell
# Install Playwright
npm install -g playwright
playwright install

# Create test file: src/test/e2e/chat.spec.js
```

Example test structure:
```javascript
test('should send message and receive bot response', async ({ page }) => {
  await page.goto('http://localhost:8080/chatbot-ui/chat.xhtml');
  await page.fill('#messageInput', 'help');
  await page.click('.send-button');
  await page.waitForSelector('.typing-indicator', { state: 'visible' });
  await page.waitForSelector('.typing-indicator', { state: 'hidden' });
  await expect(page.locator('.bot-message').last()).toContainText('Help Information');
});
```

### Cypress (Alternative)

```powershell
npm install -g cypress
```

## Troubleshooting

### Issue: `No compiler is provided in this environment` or Maven runs on Java 8
**Solution**: 
- **Root cause**: Maven is using Java 8 instead of Java 17
- **Fix 1 (Recommended)**: Use `run.ps1` or `run.bat` which sets JAVA_HOME automatically
- **Fix 2 (Manual)**: Set JAVA_HOME before running Maven:
  ```powershell
  $env:JAVA_HOME="C:\Program Files\Eclipse Adoptium\jdk-17.0.17.10-hotspot"
  $env:PATH="$env:JAVA_HOME\bin;$env:PATH"
  mvn -version  # Should show Java 17
  ```
- **Verify**: `mvn -version` should show Java 17, not Java 8
- **Note**: Even if `java -version` shows 17, Maven might still use Java 8 if JAVA_HOME isn't set

### Issue: WildFly requires Java 11+ but runs on Java 8
**Solution**:
- **Root cause**: WildFly's `standalone.conf.bat` is hardcoded to Java 8
- **Fix**: Edit `C:\wildfly\bin\standalone.conf.bat`:
  1. Find the line: `set "JAVA=...jre-1.8...\bin\java"` (or similar)
  2. Replace with: `set "JAVA=%JAVA_HOME%\bin\java"`
  3. Ensure `JAVA_HOME` is set above it: `set "JAVA_HOME=C:\Program Files\Eclipse Adoptium\jdk-17.0.17.10-hotspot"`
- **Verify**: Start WildFly manually - console should show Java 17, not Java 8
- **Note**: This affects both manual WildFly startup and Cargo (since Cargo calls WildFly scripts)

### Issue: `java: command not found`
**Solution**: Verify JAVA_HOME and PATH are set correctly. Restart PowerShell after setting environment variables.

### Issue: `mvn: command not found`
**Solution**: Verify M2_HOME and PATH are set correctly. Restart PowerShell.

### Issue: Port 8080 already in use
**Solution**: 
- Find process: `netstat -ano | findstr :8080`
- Kill process: `taskkill /PID <pid> /F`
- Or change port in `pom.xml` cargo configuration

### Issue: WAR file not deploying
**Solution**: 
- Check WildFly console for errors
- Verify WAR file exists in `target/` directory
- Check file permissions
- Ensure WildFly is running

### Issue: Page shows JSF errors
**Solution**:
- Verify all dependencies downloaded: `mvn dependency:resolve`
- Check browser console for JavaScript errors
- Ensure `web.xml` and `faces-config.xml` are correct

### Issue: Styles/JS not loading
**Solution**:
- Verify file paths in `chat.xhtml` match actual file locations
- Check browser Network tab for 404 errors
- Clear browser cache

### Issue: `mvn clean` fails - "Cannot delete target\cargo\..." (locked files)
**Solution**:
- **Root cause**: WildFly/Cargo process is still running and holding files
- **Steps**:
  1. Stop the running container: Press `Ctrl+C` in the terminal running `cargo:run`
  2. Wait a few seconds for processes to terminate
  3. Manually delete `target` folder: `Remove-Item -Recurse -Force target`
  4. Run build again: `mvn clean package cargo:run`
- **Alternative**: Close all terminals and restart

### Issue: Maven build warnings about pom.xml
**Solution**:
- If you see warnings about invalid XML tags, manually edit `pom.xml` line 13
- Change `<n>Hexabiblos Chatbot UI</n>` to `<name>Hexabiblos Chatbot UI</name>`
- This is a cosmetic issue and won't prevent the build from working

### Issue: Deployment errors - NoClassDefFoundError for javax.servlet.*
**Solution**:
- This indicates PrimeFaces is using the wrong variant (javax instead of jakarta)
- PrimeFaces 12.0.0+ has TWO variants: default (javax.*) and jakarta variant
- **IMPORTANT**: The pom.xml uses `<classifier>jakarta</classifier>` to get the Jakarta-compatible version
- If you see this error, verify the PrimeFaces dependency has the jakarta classifier:
  ```xml
  <dependency>
      <groupId>org.primefaces</groupId>
      <artifactId>primefaces</artifactId>
      <version>13.0.0</version>
      <classifier>jakarta</classifier>  <!-- This is required! -->
  </dependency>
  ```
- Clean Maven cache (PowerShell):
  ```powershell
  Remove-Item -Path "$env:USERPROFILE\.m2\repository\org\primefaces" -Recurse -Force -ErrorAction SilentlyContinue
  ```
- Clean and rebuild: `mvn clean package cargo:run`

### Issue: WildFly Management Console (port 9990) asks for username/password
**Solution**:
- This is normal - WildFly's management interface requires authentication
- To add a management user, run:
  ```powershell
  cd C:\wildfly\bin
  .\add-user.bat
  ```
- Follow the prompts:
  - Type: `a` (Application User) or `b` (Management User)
  - Username: `admin` (or your choice)
  - Password: `admin` (or your choice)
  - Groups: leave empty or press Enter
  - Confirm: `yes`
- Then access: http://localhost:9990 with your credentials
- **Note**: For Stage 1, you don't need the management console - the app runs on port 8080

## Next Steps (Future Stages)

- **Stage 3**: Add database persistence (JPA/EJB) for conversation history
- **Stage 4**: Add authentication and user management
- **Stage 5**: Add REST API endpoints for external integrations
- **Stage 6**: Add streaming responses (SSE) for real-time AI output
- **Stage 7**: Add conversation export/import functionality

## License

Internal project - All rights reserved.

## Support

For issues or questions, refer to:
- Jakarta Faces Documentation: https://jakarta.ee/specifications/faces/
- PrimeFaces Documentation: https://www.primefaces.org/documentation/
- WildFly Documentation: https://docs.wildfly.org/
