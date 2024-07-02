package scalabank.gui

import States.*
import State.*
import scalabank.bank.Bank
import scalabank.bankAccount.BankAccount
import scalabank.database.Database
import scalabank.entities.{Customer, Employee}
import scalabank.gui.SwingFunctionalFacade.Frame
import scalabank.currency.MoneyADT.toMoney
import scalabank.loan.{InterestProvider, LoanCalculator}
import scalabank.appointment.{toStringFromCustomerSide, toStringFromEmployeeSide}

import java.util.Vector as JavaVector
import java.util.List as JavaList
import java.awt.{BorderLayout, FlowLayout, GridLayout, LayoutManager}
import java.time.{LocalDate, LocalDateTime, LocalTime}
import java.util
import java.util.function.Supplier
import javax.swing.{BoxLayout, JFrame, JOptionPane}
import scala.util.{Failure, Success, Try}

trait WindowState:
  type Window
  def initialWindow: Window

  /**
   * @return the underlying JFrame
   */
  def jFrame: State[Window, JFrame]

  /**
   * Sets the size of the window
   * @param width the width of the window
   * @param height the height of the window
   * @return the updated state
   */
  def setSize(width: Int, height: Int): State[Window, Unit]

  /**
   * Adds a new view (panel) to the window
   * @param name the name of the view
   * @param layout the layout of the view
   * @return the updated state
   */
  def addView(name: String, layout: LayoutManager): State[Window, Unit]

  /**
   * Displays a view on the window
   * @param name the selected view
   * @return the updated state
   */
  def showView(name: String): State[Window, Unit]

  /**
   * Adds a panel to another panel
   * @param name the name of the panel
   * @param layout the layout of the panel
   * @param panel the container in which the panel is inserted
   * @param constraints the constraints on the new panel
   * @return the updated state
   */
  def addPanel(name: String, layout: LayoutManager, panel: String, constraints: Any): State[Window, Unit]

  /**
   * Adds a button to a panel
   * @param name the name of the button
   * @param text the text of the button
   * @param panel the container in which the button is inserted
   * @param constraints the constraints on the new button
   * @return the updated state
   */
  def addButton(name: String, text: String, panel: String, constraints: Any): State[Window, Unit]

  /**
   * Adds a label to a panel
   * @param name the name of the label
   * @param text the text of the label
   * @param panel the container in which the label is inserted
   * @param constraints the constraints on the new label
   * @return the updated state
   */
  def addLabel(name: String, text: String, panel: String, constraints: Any): State[Window, Unit]

  /**
   * Updates the text of a label
   * @param name the name of the label
   * @param text the new text
   * @return the updated state
   */
  def changeLabel(name: String, text: String): State[Window, Unit]

  /**
   * Adds an input element to a panel
   * @param name the name of the input element
   * @param columns the width of the input element in columns
   * @param panel the container in which the input element is inserted
   * @param constraints the constraints on the new input element
   * @return the updated state
   */
  def addInput(name: String, columns: Int, panel: String, constraints: Any): State[Window, Unit]

  /**
   * Returns the text inside an input element
   * @param name the name of the input element
   * @return the updated state including the text of the input element
   */
  def getInputText(name: String): State[Window, String]

  /**
   * Adds an combobox to a panel
   * @param name the name of the combobox
   * @param options the list of options for the combobox
   * @param panel the container in which the combobox is inserted
   * @param constraints the constraints on the new combobox
   * @return the updated state
   */
  def addComboBox(name: String, options: Array[String], panel: String, constraints: Any): State[Window, Unit]

  /**
   * Returns the selected element inside a combobox, which will be null in case of no selection
   * @param name the name of the combobox
   * @return the updated state including the returned selection
   */
  def getComboBoxSelection(name: String): State[Window, String]

  /**
   * Updates the contents of a combobox
   * @param name the name of the combobox
   * @param options the list of options for the combobox
   * @return the frame itself
   */
  def updateComboBox(name: String, options: Array[String]): State[Window, Unit]

  /**
   * Adds a list to a panel
   * @param name the name of the list
   * @param contents the contents of the list
   * @param panel the container in which the list is inserted
   * @param constraints the constraints on the new list
   * @return the updated state
   */
  def addList(name: String, contents: Array[String], panel: String, constraints: Any): State[Window, Unit]

  /**
   * Updates the contents of a list
   * @param name the name of the list
   * @param contents the new contents of the list
   * @return the updated state
   */
  def updateList(name: String, contents: Array[String]): State[Window, Unit]

  /**
   * Displays the window
   * @return the updated state
   */
  def show(): State[Window, Unit]

  /**
   * Creates a spacer inside a panel
   *
   * @param width       the width of the spacer
   * @param height      the height of the spacer
   * @param panel       the container in which the spacer is inserted
   * @param constraints the constraints on the spacer
   * @return the updated state
   */
  def addSpacer(width: Int, height: Int, panel: String, constraints: Any): State[Window, Unit]

  /**
   * Executes a command
   * @param cmd the command to be executed
   * @return the updated state
   */
  def exec[T](cmd: =>T): State[Window, T]

  /**
   * Returns the lazy list of events generated by the window
   * @return the updated state with the lazy list of events
   */
  def eventStream(): State[Window, LazyList[String]]

extension (so: LazyList.type)
  /**
   * Creates a lazy list whose contents are provided by a supplier
   * @param source the supplier for the elements
   * @return the lazy list of elements generated by the supplier
   */
  def generate[A](source: Supplier[A]): LazyList[A] = LazyList.iterate[A](source.get())(_ => source.get())

object WindowStateImpl extends WindowState:
  import SwingFunctionalFacade.*

  type Window = Frame

  def initialWindow: Window = createFrame

  def jFrame: State[Window, JFrame] =
    State(w => (w, w.jFrame()))
  def setSize(width: Int, height: Int): State[Window, Unit] =
    State(w => (w.setSize(width, height), {}))
  def addView(name: String, layout: LayoutManager): State[Window, Unit] =
    State(w => (w.addView(name, layout), {}))
  def showView(name: String): State[Window, Unit] =
    State(w => (w.showView(name), {}))
  def addPanel(name: String, layout: LayoutManager, panel: String, constraints: Any): State[Window, Unit] =
    State(w => (w.addPanel(name, layout, panel, constraints), {}))
  def addButton(name: String, text: String, panel: String, constraints: Any): State[Window, Unit] =
    State(w => (w.addButton(name, text, panel, constraints), {}))
  def addLabel(name: String, text: String, panel: String, constraints: Any): State[Window, Unit] =
    State(w => (w.addLabel(name, text, panel, constraints), {}))
  def changeLabel(name: String, text: String): State[Window, Unit] =
    State(w => (w.changeLabel(name, text), {}))
  def addInput(name: String, columns: Int, panel: String, constraints: Any): State[Window, Unit] =
    State(w => (w.addInput(name, columns, panel, constraints), {}))
  def getInputText(name: String): State[Window, String] =
    State(w => (w, w.getInputText(name)))
  def setInputText(name: String, text: String): State[Window, Unit] =
    State(w => (w.setInputText(name, text), {}))
  def addComboBox(name: String, options: Array[String], panel: String, constraints: Any): State[Window, Unit] =
    State(w => (w.addComboBox(name, options, panel, constraints), {}))
  def getComboBoxSelection(name: String): State[Window, String] =
    State(w => (w, w.getComboBoxSelection(name)))
  def updateComboBox(name: String, options: Array[String]): State[Window, Unit] =
    State(w => (w.updateComboBox(name, options), {}))
  def addList(name: String, contents: Array[String], panel: String, constraints: Any): State[Window, Unit] =
    State(w => (w.addList(name, JavaVector(JavaList.of(contents*)), panel, constraints), {}))
  def updateList(name: String, contents: Array[String]): State[Window, Unit] =
    State(w => (w.updateList(name, JavaVector(JavaList.of(contents*))), {}))
  def show(): State[Window, Unit] =
    State(w => (w.show, {}))
  def addSpacer(width: Int, height: Int, panel: String, constraints: Any): State[Window, Unit] =
    State(w => (w.addSpacer(width, height, panel, constraints), {}))
  def exec[T](cmd: =>T): State[Window, T] =
    State(w => (w, cmd))
  def dialog(text: String): State[Window, Unit] =
    for
      jFrame <- jFrame
      _ <- exec(JOptionPane.showMessageDialog(jFrame, text))
    yield ()
  def eventStream(): State[Window, LazyList[String]] =
    State(w => (w, LazyList.generate(w.events)))

object GUI:
  import WindowStateImpl.*
  import Monads.*, Monad.*

  def run(): Unit =
    val loginView = for
      _ <- setSize(800, 350)
      _ <- addView("Login", BorderLayout(0, 40))
      _ <- addPanel("Login-Title-Panel", FlowLayout(), "Login", BorderLayout.NORTH)
      _ <- addLabel("Login-Title", "Scalabank", "Login-Title-Panel", FlowLayout.CENTER)

      _ <- addPanel("Login-Inputs-Panel", GridLayout(1, 2), "Login", BorderLayout.CENTER)
      - <- addPanel("CF-Label-Panel", FlowLayout(), "Login-Inputs-Panel", null)
      _ <- addLabel("CF-Label", "CF:", "CF-Label-Panel", FlowLayout.CENTER)
      _ <- addInput("CF-Input", 20, "Login-Inputs-Panel", null)
      _ <- setInputText("CF-Input", "BDE")

      _ <- addPanel("Login-Button-Panel", FlowLayout(), "Login", BorderLayout.SOUTH)
      _ <- addButton("Client-Login-Button", "Login as customer", "Login-Button-Panel", FlowLayout.LEFT)
      _ <- addButton("Empl-Login-Button", "Login as employee", "Login-Button-Panel", FlowLayout.TRAILING)
    yield ()
    val userHomeView = for
      _ <- addView("User-Home", BorderLayout(0, 10))
      _ <- addPanel("User-Title-Panel", FlowLayout(), "User-Home", BorderLayout.NORTH)
      _ <- addLabel("User-Title", "Welcome ---", "User-Title-Panel", FlowLayout.CENTER)
      _ <- addPanel("User-Home-Panel", BoxLayout(null, BoxLayout.Y_AXIS), "User-Home", BorderLayout.CENTER)
      _ <- addPanel("User-Home-Panel-Row1", GridLayout(1, 2, 20, 20), "User-Home-Panel", null)
      _ <- addComboBox("User-Accounts", Array(), "User-Home-Panel-Row1", null)
      _ <- addButton("User-Home-View-Account", "See bank account", "User-Home-Panel-Row1", null)
      _ <- addSpacer(0, 20, "User-Home-Panel", null)
      _ <- addPanel("User-Home-Panel-Row2", GridLayout(1, 2, 20, 20), "User-Home-Panel", null)
      _ <- addPanel("User-Home-Panel-Row2-Inner", BoxLayout(null, BoxLayout.Y_AXIS), "User-Home-Panel-Row2", null)
      _ <- addComboBox("User-Bank-Account-Types", Array(), "User-Home-Panel-Row2-Inner", null)
      _ <- addSpacer(0, 10, "User-Home-Panel-Row2-Inner", null)
      _ <- addComboBox("User-Bank-Account-Currencies", Array(), "User-Home-Panel-Row2-Inner", null)
      _ <- addButton("User-Home-New-Account", "Open bank account", "User-Home-Panel-Row2", null)
      _ <- addSpacer(0, 20, "User-Home-Panel", null)
      _ <- addPanel("User-Home-Panel-Row3", GridLayout(1, 2, 20, 20), "User-Home-Panel", null)
      _ <- addButton("User-Home-Appointments", "Manage appointments", "User-Home-Panel-Row3", null)
      _ <- addButton("User-Home-Sim-Loan", "Loan simulator", "User-Home-Panel-Row3", null)
      _ <- addSpacer(0, 20, "User-Home-Panel", null)
      _ <- addPanel("User-Home-Panel-Row4", FlowLayout(), "User-Home-Panel", null)
      _ <- addButton("User-Home-Logout", "Logout", "User-Home-Panel-Row4", FlowLayout.CENTER)
    yield ()
    val userAccountView = for
      _ <- addView("User-Account", BorderLayout(0, 10))
      _ <- addPanel("User-Account-Title-Panel", FlowLayout(), "User-Account", BorderLayout.NORTH)
      _ <- addLabel("User-Account-Title", "Bank account ---", "User-Account-Title-Panel", FlowLayout.CENTER)
      _ <- addButton("User-Account-Back", "<", "User-Account", BorderLayout.WEST)

      _ <- addPanel("User-Account-Panel", GridLayout(3, 2, 20, 20), "User-Account", BorderLayout.CENTER)
      _ <- addPanel("Op-Amount-Panel", FlowLayout(), "User-Account-Panel", null)
      _ <- addLabel("Op-Amount-Label", "Amount:", "Op-Amount-Panel", FlowLayout.LEFT)
      _ <- addInput("Op-Amount", 10, "Op-Amount-Panel", FlowLayout.RIGHT)
      _ <- addButton("Account-Withdraw", "Withdraw", "User-Account-Panel", null)

      _ <- addPanel("Account-Amount-Panel", FlowLayout(), "User-Account-Panel", null)
      _ <- addLabel("Account-Amount", "Balance: ---", "Account-Amount-Panel", FlowLayout.CENTER)
      _ <- addButton("Account-Deposit", "Deposit", "User-Account-Panel", null)

      _ <- addPanel("Op-Target-Panel", FlowLayout(), "User-Account-Panel", null)
      _ <- addLabel("Op-Target-Label", "Receiver:", "Op-Target-Panel", FlowLayout.LEFT)
      _ <- addInput("Op-Target", 10, "Op-Target-Panel", FlowLayout.RIGHT)
      _ <- addButton("Account-Transfer", "Money transfer", "User-Account-Panel", null)

      _ <- addPanel("Op-List-Panel", BoxLayout(null, BoxLayout.Y_AXIS), "User-Account", BorderLayout.SOUTH)
      _ <- addPanel("Op-List-Label-Panel", FlowLayout(), "Op-List-Panel", null)
      _ <- addLabel("Op-List-Label", "Movements list", "Op-List-Label-Panel", FlowLayout.CENTER)
      _ <- addList("Op-List", Array(), "Op-List-Panel", null)
    yield ()
    val userAppointmentsView = for
      _ <- addView("User-Appointments", BorderLayout(0, 10))
      _ <- addButton("User-Appts-Back", "<", "User-Appointments", BorderLayout.WEST)

      _ <- addPanel("User-Appt-Panel-Outer", FlowLayout(), "User-Appointments", BorderLayout.CENTER)
      _ <- addPanel("User-Appt-Panel", GridLayout(4, 1), "User-Appt-Panel-Outer", FlowLayout.CENTER)
      _ <- addPanel("Appt-Date-Panel", FlowLayout(), "User-Appt-Panel", null)
      _ <- addLabel("Appt-Date-Label", "Date (dd-mm-yyyy):", "Appt-Date-Panel", FlowLayout.LEFT)
      _ <- addPanel("Appt-Date-Input-Panel", FlowLayout(), "Appt-Date-Panel", FlowLayout.RIGHT)
      _ <- addInput("Appt-Date-Day", 2, "Appt-Date-Input-Panel", FlowLayout.LEFT)
      _ <- addInput("Appt-Date-Month", 2, "Appt-Date-Input-Panel", FlowLayout.TRAILING)
      _ <- addInput("Appt-Date-Year", 4, "Appt-Date-Input-Panel", FlowLayout.TRAILING)
      _ <- addPanel("Appt-Time-Panel", FlowLayout(), "User-Appt-Panel", null)
      _ <- addLabel("Appt-Time-Label", "Time (hh:mm):", "Appt-Time-Panel", FlowLayout.LEFT)
      _ <- addPanel("Appt-Time-Input-Panel", FlowLayout(), "Appt-Time-Panel", FlowLayout.RIGHT)
      _ <- addInput("Appt-Time-Hours", 2, "Appt-Time-Input-Panel", FlowLayout.LEFT)
      _ <- addInput("Appt-Time-Minutes", 2, "Appt-Time-Input-Panel", FlowLayout.TRAILING)
      _ <- addLabel("Appt-Duration-Label", "Duration (minutes):", "Appt-Time-Panel", FlowLayout.LEFT)
      _ <- addInput("Appt-Duration",3, "Appt-Time-Panel", FlowLayout.RIGHT)
      _ <- addPanel("Appt-Desc-Panel", FlowLayout(), "User-Appt-Panel", null)
      _ <- addLabel("Appt-Desc-Label", "Description:", "Appt-Desc-Panel", FlowLayout.LEFT)
      _ <- addInput("Appt-Description", 20, "Appt-Desc-Panel", FlowLayout.RIGHT)
      _ <- addButton("Appt-Create", "New appointment", "User-Appt-Panel", null)

      _ <- addPanel("Appt-List-Panel", BoxLayout(null, BoxLayout.Y_AXIS), "User-Appointments", BorderLayout.SOUTH)
      _ <- addPanel("Appt-List-Label-Panel", FlowLayout(), "Appt-List-Panel", null)
      _ <- addLabel("Appt-List-Label", "Apppointments list", "Appt-List-Label-Panel", FlowLayout.CENTER)
      _ <- addList("Appt-List", Array(), "Appt-List-Panel", null)
    yield ()
    val userLoansView = for
      _ <- addView("Loan-Simulator", BorderLayout(0, 10))
      _ <- addButton("Loan-Sim-Back", "<", "Loan-Simulator", BorderLayout.WEST)

      _ <- addPanel("Loan-Inputs", FlowLayout(), "Loan-Simulator", BorderLayout.NORTH)
      _ <- addLabel("Loan-Amount-Label", "Amount:", "Loan-Inputs", FlowLayout.LEFT)
      _ <- addInput("Loan-Amount", 10, "Loan-Inputs", FlowLayout.TRAILING)
      _ <- addLabel("Loan-Months-Label", "Number of payments:", "Loan-Inputs", FlowLayout.TRAILING)
      _ <- addInput("Loan-Months", 10, "Loan-Inputs", FlowLayout.TRAILING)
      _ <- addButton("Loan-Calc", "Calculate", "Loan-Simulator", BorderLayout.CENTER)

      _ <- addPanel("Loan-Results-Outer", FlowLayout(), "Loan-Simulator", BorderLayout.SOUTH)
      _ <- addPanel("Loan-Results", GridLayout(3, 1), "Loan-Results-Outer", FlowLayout.CENTER)
      _ <- addLabel("Loan-Calc-Rate", "Monthly payment: ---", "Loan-Results", null)
      _ <- addLabel("Loan-Calc-Total", "Total payment: ---", "Loan-Results", null)
      _ <- addLabel("Loan-Calc-Interest", "Interest rate: ---", "Loan-Results", null)
    yield ()
    val emplHomeView = for
      _ <- addView("Empl-Home", BorderLayout(0, 10))

      _ <- addPanel("Empl-Title-Panel", FlowLayout(), "Empl-Home", BorderLayout.NORTH)
      _ <- addLabel("Empl-Title", "Welcome ---", "Empl-Title-Panel", FlowLayout.CENTER)

      _ <- addPanel("Empl-Panel-Outer", FlowLayout(), "Empl-Home", BorderLayout.SOUTH)
      _ <- addPanel("Empl-Panel", GridLayout(2, 1, 0, 10), "Empl-Panel-Outer", FlowLayout.CENTER)
      _ <- addButton("Empl-Home-Appointments", "Manage appointments", "Empl-Panel", null)
      _ <- addButton("Empl-Home-Logout", "Logout", "Empl-Panel", null)
    yield ()
    val emplAppointmentsView = for
      _ <- addView("Empl-Appointments", BorderLayout(0, 10))
      _ <- addPanel("Empl-Appts-Back-Panel", GridLayout(2, 1), "Empl-Appointments", BorderLayout.WEST)
      _ <- addSpacer(0, 50, "Empl-Appts-Back-Panel", null)
      _ <- addButton("Empl-Appts-Back", "<", "Empl-Appts-Back-Panel", null)

      _ <- addPanel("Empl-Appts-Panel", BoxLayout(null, BoxLayout.Y_AXIS), "Empl-Appointments", BorderLayout.CENTER)
      _ <- addSpacer(350, 50, "Empl-Appts-Panel", null)
      _ <- addPanel("Empl-Appts-Label-Panel", FlowLayout(), "Empl-Appts-Panel", null)
      _ <- addLabel("Empl-Appts-Label", "List of appointments", "Empl-Appts-Label-Panel", FlowLayout.CENTER)
      _ <- addList("Empl-Appts-List", Array(), "Empl-Appts-Panel", null)
    yield ()
    val displayView = for
      _ <- showView("Login")
      _ <- show()
      events <- eventStream()
    yield events

    var customer: Option[Customer] = None
    var employee: Option[Employee] = None
    val database = Database("jdbc:h2:./database/test")
    val bank = Bank.physicalBank("ScalaBank", "Via dell'università 1", "000-0000000")
    bank.populate(database)
    val currencies = database.currencyTable.findAll()
    InterestProvider.setInterestValues(database.interestTable.findAll().toMap)

    var account: Option[BankAccount] = None

    def addLastMovementToDb(account: BankAccount): Unit =
      val op = account.movements.last
      database.movementTable.insert(op)
      database.bankAccountTable.update(account)

    val windowEventsHandling = for
      _ <- loginView
      _ <- userHomeView
      _ <- userAccountView
      _ <- userAppointmentsView
      _ <- userLoansView
      _ <- emplHomeView
      _ <- emplAppointmentsView
      jFrame <- jFrame
      e <- displayView
      _ <- seqN:
        e.map:
          case "Client-Login-Button" =>
            for
              cf <- getInputText("CF-Input")
              _ <-
                customer = bank.customerLogin(cf)
                customer match
                  case Some(c) =>
                    for
                      _ <- changeLabel("User-Title", s"Welcome ${c.name} ${c.surname}")
                      _ <- updateComboBox("User-Accounts", c.bankAccounts.map(_.id.toString).toArray)
                      _ <- updateComboBox("User-Bank-Account-Types", bank.getBankAccountTypes.map(_.nameType).toArray)
                      _ <- updateComboBox("User-Bank-Account-Currencies", currencies.map(_.toString).toArray)
                      _ <- showView("User-Home")
                    yield ()
                  case None =>
                    dialog("Unknown user.")
            yield ()
          case "User-Home-View-Account" =>
            for
              accId <- getComboBoxSelection("User-Accounts")
              _ <- changeLabel("User-Account-Title", s"Bank account $accId")
              opt <- exec:
                customer.get.bankAccounts.find(_.id.toString == accId)
              _ <-
                opt match
                  case Some(_) =>
                    exec:
                      account = opt
                  case _ =>
                    dialog("No bank account available.")
              _ <- changeLabel("Account-Amount", s"Balance: ${account.get.balance.toString} ${account.get.currency.symbol}")
              _ <- updateList(
                "Op-List",
                account.get.movements.map(_.toString).toArray
              )
              _ <- showView("User-Account")
            yield ()
          case "Account-Withdraw" =>
            for
              amount <- getInputText("Op-Amount")
              money = Try(amount.toMoney)
              _ <-
                money match
                  case Success(m) =>
                    for
                      _ <-
                        if account.get.withdraw(m)
                        then
                          for
                            _ <- exec:
                              addLastMovementToDb(account.get)
                            _ <- changeLabel("Account-Amount", s"Balance: ${account.get.balance.toString} ${account.get.currency.symbol}")
                            _ <- updateList(
                              "Op-List",
                              account.get.movements.map(_.toString).toArray
                            )
                            _ <- setInputText("Op-Amount", "")
                          yield ()
                        else
                          dialog("Withdraw failed.")
                    yield ()
                  case Failure(_) =>
                    for
                      _ <- dialog("Invalid withdraw amount.")
                    yield ()
            yield ()
          case "Account-Deposit" =>
            for
              amount <- getInputText("Op-Amount")
              money = Try(amount.toMoney)
              _ <-
                money match
                  case Success(m) =>
                    for
                      _ <- exec:
                        account.get.deposit(m)
                      _ <- exec:
                        addLastMovementToDb(account.get)
                      _ <- changeLabel("Account-Amount", s"Balance: ${account.get.balance.toString} ${account.get.currency.symbol}")
                      _ <- updateList(
                        "Op-List",
                        account.get.movements.map(_.toString).toArray
                      )
                      _ <- setInputText("Op-Amount", "")
                    yield ()
                  case Failure(_) =>
                    for
                      _ <- dialog("Invalid deposit amount.")
                    yield ()
            yield ()
          case "Account-Transfer" =>
            for
              amount <- getInputText("Op-Amount")
              destIdStr <- getInputText("Op-Target")
              optDest <- exec:
                destIdStr.toIntOption
              money = Try(amount.toMoney)
              _ <-
                (optDest, money) match
                  case (Some(destId), Success(m)) =>
                    for
                      dest <- exec:
                        bank.findBankAccount(destId)
                      _ <-
                        dest match
                          case Some(d) =>
                            if account.get.makeMoneyTransfer(d, m)
                            then
                              for
                                _ <- exec:
                                  addLastMovementToDb(account.get)
                                _ <- changeLabel("Account-Amount", s"Balance: ${account.get.balance.toString} ${account.get.currency.symbol}")
                                _ <- updateList(
                                  "Op-List",
                                  account.get.movements.map(_.toString).toArray
                                )
                                _ <- setInputText("Op-Amount", "")
                                _ <- setInputText("Op-Target", "")
                              yield ()
                            else
                              dialog("Money transfer failed.")
                          case None =>
                            dialog("Destination bank account not found.")
                    yield ()
                  case _ =>
                    for
                      _ <- dialog("Invalid transfer amount and/or destination bank account.")
                    yield ()
            yield ()
          case "User-Home-New-Account" =>
            for
              accTypeName <- getComboBoxSelection("User-Bank-Account-Types")
              currencyStr <- getComboBoxSelection("User-Bank-Account-Currencies")
              currency <- exec:
                currencies.find(_.toString == currencyStr).get
              accType <- exec:
                bank.getBankAccountTypes.find(_.nameType == accTypeName).get
              acc <- exec:
                bank.createBankAccount(customer.get, accType, currency)
              _ <- exec:
                database.bankAccountTable.insert(acc)
                database.customerTable.update(customer.get)
              _ <- updateComboBox("User-Accounts", customer.get.bankAccounts.map(_.id.toString).toArray)
              _ <- changeLabel("User-Account-Title", s"Bank account ${acc.id}")
              _ <- exec:
                account = Option(acc)
              _ <- changeLabel("Account-Amount", s"Balance: ${account.get.balance.toString} ${account.get.currency.symbol}")
              _ <- updateList(
                "Op-List",
                account.get.movements.map(_.toString).toArray
              )
              _ <- showView("User-Account")
            yield ()
          case "User-Home-Sim-Loan" =>
            for
              _ <- setInputText("Loan-Amount", "")
              _ <- setInputText("Loan-Months", "")
              _ <- changeLabel("Loan-Calc-Rate", "Monthly payment: ---")
              _ <- changeLabel("Loan-Calc-Total", "Total payment: ---")
              _ <- changeLabel("Loan-Calc-Interest", "Interest rate: ---")
              _ <-showView("Loan-Simulator")
            yield ()
          case "Loan-Calc" =>
            for
              loanCalculator <- exec:
                LoanCalculator()
              amount <- getInputText("Loan-Amount")
              months <- getInputText("Loan-Months")
              _ <-
                (Try(amount.toMoney), Try(months.toInt)) match
                  case (Success(a), Success(m)) if m > 0 =>
                    for
                      res <- exec:
                        loanCalculator.calculateLoan(customer.get, a, m)
                      _ <- changeLabel("Loan-Calc-Rate", s"Monthly payment: ${res.amountOfSinglePayment.toBigDecimal.setScale(2, BigDecimal.RoundingMode.HALF_EVEN)}")
                      _ <- changeLabel("Loan-Calc-Total", s"Total payment: ${res.totalAmount.toBigDecimal.setScale(2, BigDecimal.RoundingMode.HALF_EVEN)}")
                      _ <- changeLabel("Loan-Calc-Interest", s"Interest rate: ${res.interestRate.toString}")
                    yield ()
                  case _ =>
                    dialog("Invalid loan amount or number of months.")
            yield ()
          case "Empl-Login-Button" =>
            for
              cf <- getInputText("CF-Input")
              _ <-
                employee = bank.employeeLogin(cf)
                employee match
                  case Some(e) =>
                    for
                      _ <- changeLabel("Empl-Title", s"Welcome ${e.name} ${e.surname}")
                      _ <- showView("Empl-Home")
                    yield ()
                  case None =>
                    dialog("Unknown employee.")
            yield ()
          case "Empl-Home-Appointments" =>
            for
              _ <- updateList("Empl-Appts-List", employee.get.getAppointments.map(_.toStringFromEmployeeSide).toArray)
              _ <- showView("Empl-Appointments")
            yield ()
          case "User-Home-Appointments" =>
            for
              _ <- updateList("Appt-List", customer.get.getAppointments.map(_.toStringFromCustomerSide).toArray)
              _ <- showView("User-Appointments")
            yield ()
          case "Appt-Create" =>
            for
              dayStr <- getInputText("Appt-Date-Day")
              monthStr <- getInputText("Appt-Date-Month")
              yearStr <- getInputText("Appt-Date-Year")
              hoursStr <- getInputText("Appt-Time-Hours")
              minutesStr <- getInputText("Appt-Time-Minutes")
              durationStr <- getInputText("Appt-Duration")
              description <- getInputText("Appt-Description")
              _ <-
                (Try(dayStr.toInt), Try(monthStr.toInt), Try(yearStr.toInt), Try(hoursStr.toInt), Try(minutesStr.toInt), Try(durationStr.toInt)) match
                  case (Success(day), Success(month), Success(year), Success(hours), Success(mins), Success(duration)) if duration > 0 =>
                    Try(LocalDateTime.of(LocalDate.of(year, month, day), LocalTime.of(hours, mins))) match
                      case Success(date) if date.isAfter(LocalDateTime.now) =>
                        for
                          appt <- exec:
                            bank.createAppointment(customer.get, description, date, duration)
                          _ <- exec:
                            database.appointmentTable.insert(appt)
                            database.customerTable.update(appt.customer)
                            database.employeeTable.update(appt.employee)
                          _ <- setInputText("Appt-Date-Day", "")
                          _ <- setInputText("Appt-Date-Month", "")
                          _ <- setInputText("Appt-Date-Year", "")
                          _ <- setInputText("Appt-Time-Hours", "")
                          _ <- setInputText("Appt-Time-Minutes", "")
                          _ <- setInputText("Appt-Duration", "")
                          _ <- setInputText("Appt-Description", "")
                          _ <- updateList("Appt-List", customer.get.getAppointments.map(_.toStringFromCustomerSide).toArray)
                          _ <- dialog("Appointment created successfully.")
                        yield ()
                      case _ =>
                        dialog("The chosen date for the appointment is invalid.")
                  case _ =>
                    dialog("One or more parameters are invalid.")
            yield ()
          case "User-Home-Logout" => showView("Login")
          case "Empl-Home-Logout" => showView("Login")
          case "User-Account-Back" => showView("User-Home")
          case "User-Appts-Back" => showView("User-Home")
          case "Loan-Sim-Back" => showView("User-Home")
          case "Empl-Appts-Back" => showView("Empl-Home")
          case Frame.CLOSED => exec(sys.exit())
          case v => exec(println(s"No action: $v"))
    yield ()

    try
      windowEventsHandling.run(initialWindow)
    catch {
      case e: Throwable =>
        val error = for
          _ <- exec:
            e.printStackTrace()
          _ <- dialog(e.toString)
          _ <- exec:
            System.exit(1)
        yield ()
        error.run(initialWindow)
    }
