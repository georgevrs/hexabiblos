# Hexabiblos Enterprise Chatbot UI - Stage 1

A polished, professional chatbot web application built with Jakarta EE (JSF + PrimeFaces) for enterprise environments. This is Stage 1: a fully functional UI with client-side simulated chat logic, requiring no backend services, databases, or API keys.

## Project Overview

**Technology Stack:**
- Jakarta Faces (JSF) 4.0.1
- PrimeFaces 12.0.0
- Maven 3.x
- Java 17
- WildFly 27.x (preferred) or Tomcat 10.x

**Stage 1 Features:**
- ✅ Enterprise-grade responsive UI
- ✅ Real-time message display with user/bot styling
- ✅ Typing indicator simulation (600-1200ms delay)
- ✅ Quick reply chips (Help, Show Features, Reset)
- ✅ Enter key to send (Shift+Enter for newline)
- ✅ Auto-scroll to latest message
- ✅ Clear chat functionality
- ✅ Keyword-based canned responses
- ✅ No backend dependencies (pure client-side simulation)

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

1. **Open PowerShell or Command Prompt**
2. **Navigate to project directory:**
   ```powershell
   cd C:\Users\georg\Desktop\Interviews\Hexabiblos
   ```
3. **Build the WAR file:**
   ```powershell
   mvn clean package
   ```
   This will:
   - Download all dependencies (first time may take a few minutes)
   - Compile Java code (if any)
   - Package the WAR file to `target/chatbot-ui.war`

## Running the Application

### Method 1: Embedded WildFly (Easiest - Recommended for Stage 1)

```powershell
mvn clean package cargo:run
```

This will:
- Build the WAR
- Start an embedded WildFly server
- Deploy the application
- Open browser automatically (or navigate to http://localhost:8080/chatbot-ui/chat.xhtml)

**To stop:** Press `Ctrl+C` in the terminal

### Method 2: Manual WildFly Deployment

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

### Method 3: Using Tomcat 10.x (Alternative)

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

- **Stage 2**: Add backend REST API with Jakarta REST
- **Stage 3**: Integrate Gemini API for real LLM responses
- **Stage 4**: Add database persistence (JPA/EJB)
- **Stage 5**: Add authentication and user management
- **Stage 6**: Add conversation history and context management

## License

Internal project - All rights reserved.

## Support

For issues or questions, refer to:
- Jakarta Faces Documentation: https://jakarta.ee/specifications/faces/
- PrimeFaces Documentation: https://www.primefaces.org/documentation/
- WildFly Documentation: https://docs.wildfly.org/
