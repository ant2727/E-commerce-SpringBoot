#!/usr/bin/env node
/**
 * Remove --trailer Co-authored-by: Cursor from Shell git commit commands
 * before they run (Cursor injects this automatically).
 */
const fs = require("fs");

let raw = "";
try {
  raw = fs.readFileSync(0, "utf8");
} catch {
  process.stdout.write(JSON.stringify({ permission: "allow" }));
  process.exit(0);
}

if (!raw.trim()) {
  process.stdout.write(JSON.stringify({ permission: "allow" }));
  process.exit(0);
}

let payload;
try {
  payload = JSON.parse(raw);
} catch {
  process.stdout.write(JSON.stringify({ permission: "allow" }));
  process.exit(0);
}

const toolInput = payload.tool_input || payload.input || {};
const command = typeof toolInput.command === "string" ? toolInput.command : "";

const trailerPatterns = [
  /\s*--trailer\s+"Co-authored-by:\s*Cursor\s*<cursoragent@cursor\.com>"/gi,
  /\s*--trailer\s+'Co-authored-by:\s*Cursor\s*<cursoragent@cursor\.com>'/gi,
  /\s*--trailer\s+"Co-authored-by:[^"]*cursor[^"]*"/gi,
  /\s*--trailer\s+'Co-authored-by:[^']*cursor[^']*'/gi,
  /\s*--trailer\s+Co-authored-by:\s*Cursor\s*<cursoragent@cursor\.com>/gi,
];

let cleaned = command;
for (const pattern of trailerPatterns) {
  cleaned = cleaned.replace(pattern, "");
}

if (cleaned !== command) {
  process.stdout.write(
    JSON.stringify({
      permission: "allow",
      updated_input: {
        ...toolInput,
        command: cleaned,
      },
    })
  );
} else {
  process.stdout.write(JSON.stringify({ permission: "allow" }));
}
