package com.github.xiaolyuh.ui;

import com.github.xiaolyuh.action.options.InitOptions;
import com.github.xiaolyuh.i18n.I18n;
import com.github.xiaolyuh.i18n.I18nKey;
import com.github.xiaolyuh.i18n.LanguageEnum;
import com.github.xiaolyuh.utils.ConfigUtil;
import com.github.xiaolyuh.utils.GitBranchUtil;
import com.google.common.collect.Lists;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.openapi.ui.ValidationInfo;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.ui.CollectionComboBoxModel;

import javax.annotation.Nullable;
import javax.swing.*;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * 初始化插件弹框
 *
 * @author yuhao.wang3
 */
public class InitPluginDialog extends DialogWrapper {
    private JPanel contentPane;

    private JComboBox<String> masterBranchComboBox;
    private JComboBox<String> testBranchComboBox;
    private JTextField featurePrefixTextField;
    private JTextField hotfixPrefixTextField;
    private JLabel specialBranchConfigLabel;
    private JLabel mastBranchLabel;
    private JLabel testBranchLabel;
    private JLabel branchPrefixConfigLabel;
    private JLabel featureBranchPrefixLabel;
    private JLabel hotfixBranchPrefixLabel;

    public InitPluginDialog(Project project) {
        super(project);
        I18n.init(project);

        setTitle(I18n.getContent(I18nKey.INIT_PLUGIN_DIALOG$TITLE));

        initDialog(project);

        init();
    }

    private void languageSwitch() {
        setTitle(I18n.getContent(I18nKey.INIT_PLUGIN_DIALOG$TITLE));

        specialBranchConfigLabel.setText(I18n.getContent(I18nKey.INIT_PLUGIN_DIALOG$SPECIAL_BRANCH_CONFIG_LABEL));
        mastBranchLabel.setText(I18n.getContent(I18nKey.INIT_PLUGIN_DIALOG$MAST_BRANCH_LABEL));
        testBranchLabel.setText(I18n.getContent(I18nKey.INIT_PLUGIN_DIALOG$TEST_BRANCH_LABEL));
        branchPrefixConfigLabel.setText(I18n.getContent(I18nKey.INIT_PLUGIN_DIALOG$BRANCH_PREFIX_CONFIG_LABEL));
        featureBranchPrefixLabel.setText(I18n.getContent(I18nKey.INIT_PLUGIN_DIALOG$FEATURE_BRANCH_PREFIX_LABEL));
        hotfixBranchPrefixLabel.setText(I18n.getContent(I18nKey.INIT_PLUGIN_DIALOG$HOTFIX_BRANCH_PREFIX_LABEL));
    }


    public InitOptions getOptions() {
        InitOptions options = new InitOptions();

        options.setMasterBranch((String) masterBranchComboBox.getSelectedItem());
        options.setTestBranch((String) testBranchComboBox.getSelectedItem());
        options.setFeaturePrefix(featurePrefixTextField.getText());
        options.setHotfixPrefix(hotfixPrefixTextField.getText());

        return options;
    }

    /**
     * 初始化弹框
     */
    private void initDialog(Project project) {
        Optional<InitOptions> options = ConfigUtil.getConfig(project);
        List<String> remoteBranches = GitBranchUtil.getRemoteBranches(project);
        List<String> languages = LanguageEnum.getAllLanguage();

        if (options.isPresent()) {
            List<String> masterBranchList = Lists.newArrayList(options.get().getMasterBranch());
            List<String> releaseBranchList = Lists.newArrayList(options.get().getReleaseBranch());
            List<String> testBranchList = Lists.newArrayList(options.get().getTestBranch());
            masterBranchList.addAll(remoteBranches);
            releaseBranchList.addAll(remoteBranches);
            testBranchList.addAll(remoteBranches);
            masterBranchComboBox.setModel(new CollectionComboBoxModel<>(remoteBranches, options.get().getMasterBranch()));
            testBranchComboBox.setModel(new CollectionComboBoxModel<>(remoteBranches, options.get().getTestBranch()));

            featurePrefixTextField.setText(options.get().getFeaturePrefix());
            hotfixPrefixTextField.setText(options.get().getHotfixPrefix());

            languageSwitch();
        } else {

            masterBranchComboBox.setModel(new CollectionComboBoxModel<>(remoteBranches));
            testBranchComboBox.setModel(new CollectionComboBoxModel<>(remoteBranches));
        }
    }

    @Override
    protected void doOKAction() {
        super.doOKAction();
    }

    @Override
    public void doCancelAction() {
        super.doCancelAction();
    }

    @Nullable
    @Override
    protected ValidationInfo doValidate() {
        if (Objects.equals(masterBranchComboBox.getSelectedItem(), testBranchComboBox.getSelectedItem())) {
            return new ValidationInfo(I18n.getContent(I18nKey.INIT_PLUGIN_DIALOG$TEST_LIKE_MASTER), testBranchComboBox);
        }
        if (StringUtil.isEmptyOrSpaces(featurePrefixTextField.getText())) {
            return new ValidationInfo(I18n.getContent(I18nKey.INIT_PLUGIN_DIALOG$FEATURE_PREFIX_REQUIRED), featurePrefixTextField);
        }
        if (StringUtil.isEmptyOrSpaces(hotfixPrefixTextField.getText())) {
            return new ValidationInfo(I18n.getContent(I18nKey.INIT_PLUGIN_DIALOG$HOTFIX_PREFIX_REQUIRED), hotfixPrefixTextField);
        }
        return null;
    }

    @Nullable
    @Override
    protected JComponent createCenterPanel() {
        return contentPane;
    }
}
