package br.ce.ogj.core;

import static br.ce.ogj.core.DriverFactory.getDriver;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

public class BasePage {
/********* TextField e TextArea ************/
	
	public void escreveBy(By by, String text) {
		getDriver().findElement(by).clear();
		getDriver().findElement(by).sendKeys(text);
	}
	
	public void escreveById(String id, String text) {
		getDriver().findElement(By.id(id)).clear();
		getDriver().findElement(By.id(id)).sendKeys(text);
	}
	
	public String obterValorCampo(By by) {
		return getDriver().findElement(by).getAttribute("value");
	}
	
	/********* Radio e Check ************/
	public void clicarBy(By by) {
		getDriver().findElement(by).click();
	}
	public void submitBy(By by) {
		getDriver().findElement(by).submit();
	}
	public void clicarById(String id) {
		clicarBy(By.id(id));
	}
	public Boolean isMarcadoBy(By by) {
		return getDriver().findElement(by).isSelected();
	}
	public Boolean isMarcadoById(String id) {
		return isMarcadoBy(By.id(id));
	}
	
	
	/********* Combo ************/
	public void selectComboBy(By by, String text) {
		WebElement element = getDriver().findElement(by);
		Select combo = new Select(element);
		combo.selectByVisibleText(text);
	}
	public void deselectCombo(String id,String text) {
		WebElement element = getDriver().findElement(By.id(id));
		Select combo = new Select(element);
		combo.deselectByVisibleText(text);
	}
	public String obterValorCombo(String id) {
		WebElement element = getDriver().findElement(By.id(id));
		Select combo = new Select(element);
		return combo.getFirstSelectedOption().getText();
	}
	public List<String> obterValoresCombo(String id) {
		WebElement element = getDriver().findElement(By.id(id));
		Select combo = new Select(element);
		List<WebElement> allSelectedOptions = combo.getAllSelectedOptions();
		List<String> valores = new ArrayList<String>();
		for(WebElement opcao: allSelectedOptions) {
			valores.add(opcao.getText());
		}
		return valores;
	}
	public int obterQuantidadeOpcoesCombo(String id){
		WebElement element = getDriver().findElement(By.id(id));
		Select combo = new Select(element);
		List<WebElement> options = combo.getOptions();
		return options.size();
	}
	public boolean verificarOpcaoCombo(String id, String opcao){
		WebElement element = getDriver().findElement(By.id(id));
		Select combo = new Select(element);
		List<WebElement> options = combo.getOptions();
		for(WebElement option: options) {
			if(option.getText().equals(opcao)){
				return true;
			}
		}
		return false;
	}
	public void selectComboPrime(By by, By by2) {
		getDriver().findElement(by).click();
		getDriver().findElement(by2).click();
	}
	public String comboSelecionado(String id) {
		WebElement element = getDriver().findElement(By.id(id));
		Select combo = new Select(element);
		return combo.getFirstSelectedOption().getText();
	}
	/********* Obter Valores ************/
	public String buttonAttribute(String id) {
		WebElement botao = getDriver().findElement(By.id(id));
		return botao.getAttribute("value");
	}
	public String selectByBy(By by) {
		return getDriver().findElement(by).getText();	
	}
	/********* Link ************/
	public void linkClick(String linkText) {
		getDriver().findElement(By.linkText(linkText)).click();
	}
	/********* Textos ************/
	
	public String getTextByID(String id) {
		return getDriver().findElement(By.id(id)).getText();
	}
	
	public String getTextBy(By by) {
		return getDriver().findElement(by).getText();
	}

	/********* Alert ************/
	private Alert alert;
	
	public Alert alertWait() {
		WebDriverWait wait = new WebDriverWait(getDriver(), Duration.ofSeconds(5));
	    alert = wait.until(ExpectedConditions.alertIsPresent());
	    return alert;
	}
	public void aceitarAlert() {
		alertWait();
		alert.accept();	
	}
	public void recusarAlert() {
		alertWait();
		alert.dismiss();	
	}
	public String alertObterTexto() {
	    alertWait();
	    return alert.getText();
	}
	public String alertObterTextoEAceita() {
		String text = alertObterTexto();
		aceitarAlert();
		return text;
	}
	public String alertObterTextoENega() {
		String text = alertObterTexto();
		recusarAlert();
		return text;
	}	
	public void verificarTextoAlert(String text) {
		Assert.assertEquals(text, alertObterTexto());
	}
	public void escreverAlert(String text) {
		alertWait();
		alert.sendKeys(text);
	}
	/********* Frames e Janelas ************/
	
	public void entrarFrame(String idFrame) {
		getDriver().switchTo().frame(idFrame);	
	}
	
	public void sairFrame() {
		getDriver().switchTo().defaultContent();	
	}
	public void trocarJanela(String id) {
		getDriver().switchTo().window(id);
	}
	
	/********* Verificacoes ************/

	public void comparaçãoTexto(String text, String id) {
		
		Assert.assertEquals(text, getTextBy(By.id(id)));
	}
	public void comparaçãoTextoBy(String text, By by) {
		Assert.assertEquals(text, getTextBy(by));
	}
	public void comparaçãoTextoValue(String text, String id) {
		
		Assert.assertEquals(text, getDriver().findElement(By.id(id)).getAttribute("value"));
	}
	
	
	/**************** JS ****************/
	public Object executarJS(String cmd, Object... param) {
		JavascriptExecutor js = (JavascriptExecutor) getDriver();
		return js.executeScript(cmd, param);
	}
	
	/**************** Tabela ****************/
	public WebElement getTableCell(String searchColunm, String value, String colunmButton, String idTable) {
			//Search for Column Table
			WebElement table = getDriver().findElement(By.xpath("//*[@id='"+ idTable +"']"));
			int idColumn = getColumnIndex(searchColunm, table);
			
			//Search for Line Table
			int idLine = getLineIndex(value, table, idColumn);
			
			//Search for column button
			int idColumnButton = getColumnIndex(colunmButton, table);
			
			//Search for Cell
			WebElement cell = table.findElement(By.xpath(".//tr["+idLine+"]/td["+idColumnButton+"]"));
			return cell;
		}
	public void clickInTableButton(String searchColunm, String value, String colunmButton, String idTable) {
		
		WebElement cell = getTableCell(searchColunm, value, colunmButton, idTable);
		cell.findElement(By.xpath("./input")).click();
	}
	

	private int getLineIndex(String value, WebElement table, int idColumn) {
		List<WebElement> lines = table.findElements(By.xpath("./tbody/tr/td["+idColumn+"]"));
		int idLine = -1;
		for(int i = 0; i < lines.size(); i++) {
			if(lines.get(i).getText().equals(value)) {
				idLine = i+1;
				break;
			}
		}
		return idLine;
	}

	private int getColumnIndex(String Column, WebElement tabela) {
		List<WebElement> columns = tabela.findElements(By.xpath(".//th"));
		int idColumn = -1;
		for(int i = 0; i < columns.size(); i++) {
			if(columns.get(i).getText().equals(Column)) {
				idColumn = i+1;
				break;
			}	
		}
		return idColumn;
	}
}
