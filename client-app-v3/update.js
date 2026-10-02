const fs = require('fs');
const path = require('path');

const dir = 'd:/GLOBITS_REACT/client-app-v3/src/app/views';

function processDir(directory) {
    const files = fs.readdirSync(directory);
    for (const file of files) {
        const fullPath = path.join(directory, file);
        if (fs.statSync(fullPath).isDirectory()) {
            processDir(fullPath);
        } else if (fullPath.endsWith('Index.jsx') && !fullPath.includes('dashboard')) {
            let content = fs.readFileSync(fullPath, 'utf8');
            
            const regex = /<div style=\{\{ display: 'flex', alignItems: 'center' \}\}>\s*<h2 style=\{\{ margin: 0, marginRight: '16px' \}\}>(.*?)<\/h2>\s*<Button variant='contained' color='primary' onClick=\{handleAddItem\}>(.*?)<\/Button>\s*<\/div>/g;
            
            const newContent = content.replace(regex, (match, title, btnText) => {
                return `<div style={{ display: 'flex', flexDirection: 'column', alignItems: 'flex-start' }}>
                            <h2 style={{ margin: 0, marginBottom: '16px' }}>${title}</h2>
                            <Button variant='contained' color='primary' onClick={handleAddItem}>${btnText}</Button>
                        </div>`;
            });
            
            if (content !== newContent) {
                fs.writeFileSync(fullPath, newContent);
                console.log('Updated: ' + fullPath);
            } else {
                console.log('Skipped/No match: ' + fullPath);
            }
        }
    }
}
processDir(dir);
